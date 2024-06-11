import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RequestTest {
    
    @Test
    void testSend_Success() {
        // Arrange
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Act
        Response response = request.send();

        // Assert
        assertNotNull(response);
        // Additional assertions for successful response
    }

    @Test
    void testSend_CreateConnectionIOException() {
        // Arrange
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Act/Assert
        assertThrows(OAuthException.class, () -> {
            request.send();
        });
    }

    @Test
    void testCreateConnection_ConnectionIsNull() {
        // Arrange
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.setConnection(null);

        // Act
        assertDoesNotThrow(() -> {
            request.createConnection();
        });
    }

    @Test
    void testDoSend_PutVerb_Success() {
        // Arrange
        Verb verb = Verb.PUT;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Act
        Response response = request.doSend();

        // Assert
        assertNotNull(response);
        // Additional assertions for successful response and PUT verb
    }

    @Test
    void testDoSend_PostVerb_Success() {
        // Arrange
        Verb verb = Verb.POST;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Act
        Response response = request.doSend();

        // Assert
        assertNotNull(response);
        // Additional assertions for successful response and POST verb
    }
    
    // Additional test cases for each method in the Request class
    
}