package app.ksoft.functions.corona;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class VaccinationsGermany {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("VaccinationsGermany")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);
        String incidenceCsvString = "Stunde,Montag,Dienstag,Mittwoch,Donnerstag,Freitag\n" +
                "1,Mathematik,Deutsch,Englisch,Erdkunde,Politik";
        incidenceCsvString.getBytes();
        if (name == null) {
            // CSV
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Disposition", "attachment; filename=" + "incidence.csv")
                    .body(incidenceCsvString.getBytes()).build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }

    }

    private HttpResponseMessage createServerError(HttpRequestMessage<Optional<String>> request,
                                                  String message) {
        if(message == null) {
            message = "the server has a problem!";
        }
        return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR. Sorry, " + message).build();
    }
}
