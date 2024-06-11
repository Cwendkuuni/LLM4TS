import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.net.*;
import java.io.*;
import org.scribe.model.Response;

public class ResponseTest {

    @Test
    public void testResponseConstructor() {
        // Arrange
        HttpURLConnection connection = null;

        // Act
        Response response = new Response(connection);

        // Assert
        assertNotNull(response);
    }

    @Test
    public void testGetBody() {
        // Arrange
        Response response = new Response(null);

        // Act
        String body = response.getBody();

        // Assert
        assertNotNull(body);
        assertEquals("", body);
    }

    @Test
    public void testGetStream() {
        // Arrange
        Response response = new Response(null);

        // Act
        InputStream stream = response.getStream();

        // Assert
        assertNotNull(stream);
    }

    @Test
    public void testGetCode() {
        // Arrange
        Response response = new Response(null);

        // Act
        int code = response.getCode();

        // Assert
        assertEquals(0, code);
    }

    @Test
    public void testGetHeaders() {
        // Arrange
        Response response = new Response(null);

        // Act
        Map<String, String> headers = response.getHeaders();

        // Assert
        assertNotNull(headers);
        assertTrue(headers.isEmpty());
    }

    @Test
    public void testGetHeader() {
        // Arrange
        Response response = new Response(null);
        String headerName = "Content-Type";

        // Act
        String headerValue = response.getHeader(headerName);

        // Assert
        assertNull(headerValue);
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testResponseConstructorWithValidConnection() {
        // Arrange
        HttpURLConnection connection = createValidConnection();

        // Act
        Response response = new Response(connection);

        // Assert
        assertNotNull(response);
        // Add more assertions if needed
    }

    @Test
    public void testResponseConstructorWithNullConnection() {
        // Arrange
        HttpURLConnection connection = null;

        // Act
        Response response = new Response(connection);

        // Assert
        assertNotNull(response);
        // Add more assertions if needed
    }

    @Test
    public void testGetBodyWithSpecialCharacters() {
        // Arrange
        Response response = createResponseWithSpecialCharacters();

        // Act
        String body = response.getBody();

        // Assert
        assertNotNull(body);
        // Add more assertions if needed
    }

    @Test
    public void testGetCodeWithDifferentResponseCodes() {
        // Arrange
        Response response1 = createResponseWithCode(200);
        Response response2 = createResponseWithCode(404);
        Response response3 = createResponseWithCode(500);

        // Act
        int code1 = response1.getCode();
        int code2 = response2.getCode();
        int code3 = response3.getCode();

        // Assert
        assertEquals(200, code1);
        assertEquals(404, code2);
        assertEquals(500, code3);
    }

    @Test
    public void testGetHeadersWithDifferentCombinations() {
        // Arrange
        Response response1 = createResponseWithHeaders(createHeaders("Content-Type: application/json"));
        Response response2 = createResponseWithHeaders(createHeaders("Content-Type: text/html", "X-Custom-Header: value"));
        Response response3 = createResponseWithHeaders(createHeaders());

        // Act
        Map<String, String> headers1 = response1.getHeaders();
        Map<String, String> headers2 = response2.getHeaders();
        Map<String, String> headers3 = response3.getHeaders();

        // Assert
        assertEquals(1, headers1.size());
        assertEquals(2, headers2.size());
        assertTrue(headers2.containsKey("Content-Type"));
        assertTrue(headers2.containsKey("X-Custom-Header"));
        assertTrue(headers3.isEmpty());
    }

    @Test
    public void testGetHeaderWithDifferentNames() {
        // Arrange
        Response response = createResponseWithHeaders(createHeaders("Content-Type: application/json", "X-Custom-Header: value"));

        // Act
        String header1 = response.getHeader("Content-Type");
        String header2 = response.getHeader("X-Custom-Header");
        String header3 = response.getHeader("Unknown-Header");

        // Assert
        assertEquals("application/json", header1);
        assertEquals("value", header2);
        assertNull(header3);
    }

    // Utility methods for test cases

    private HttpURLConnection createValidConnection() {
        // Create and return a valid HttpURLConnection object
    }

    private Response createResponseWithSpecialCharacters() {
        // Create and return a Response object with a response body containing special characters
    }

    private Response createResponseWithCode(int code) {
        // Create and return a Response object with the specified response code
    }

    private Response createResponseWithHeaders(Map<String, String> headers) {
        // Create and return a Response object with the specified headers
    }

    private Map<String, List<String>> createHeaders(String... headerInputs) {
        // Create and return a map of headers based on the given inputs
    }
}