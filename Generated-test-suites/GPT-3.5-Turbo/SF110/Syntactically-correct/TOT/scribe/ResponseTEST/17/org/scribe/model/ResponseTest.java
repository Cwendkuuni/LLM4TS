import org.junit.Test;
import org.scribe.model.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testConstructor_givenValidConnection_responseCodeSetCorrectly() throws IOException {
        // Arrange
        HttpURLConnection mockConnection = createMockConnection(200);

        // Act
        Response response = new Response(mockConnection);

        // Assert
        assertEquals(200, response.getCode());
    }

    @Test
    public void testConstructor_givenInvalidConnection_responseCodeSetTo404() throws IOException {
        // Arrange
        HttpURLConnection mockConnection = createMockConnection(UnknownHostException.class);

        // Act
        Response response = new Response(mockConnection);

        // Assert
        assertEquals(404, response.getCode());
    }

    @Test
    public void testConstructor_givenValidConnectionAndSuccessfulResponse_streamIsAssignedToInputStream() throws IOException {
        // Arrange
        HttpURLConnection mockConnection = createMockConnection(200);

        // Act
        Response response = new Response(mockConnection);

        // Assert
        assertNotNull(response.getStream());
    }

    @Test
    public void testConstructor_givenValidConnectionAndUnsuccessfulResponse_streamIsAssignedToErrorStream() throws IOException {
        // Arrange
        HttpURLConnection mockConnection = createMockConnection(500);

        // Act
        Response response = new Response(mockConnection);

        // Assert
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetBody_whenBodyIsNull_parseBodyContentsIsCalled() throws IOException {
        // Arrange
        Response response = createResponseWithNullBody();

        // Act
        String body = response.getBody();

        // Assert
        assertEquals("Test Body", body);
    }

    @Test
    public void testGetBody_whenBodyIsNotNull_returnBody() throws IOException {
        // Arrange
        Response response = createResponseWithNonNullBody();

        // Act
        String body = response.getBody();

        // Assert
        assertEquals("Test Body", body);
    }

    @Test
    public void testGetStream_returnsStream() throws IOException {
        // Arrange
        Response response = createResponseWithNonNullStream();

        // Act
        InputStream stream = response.getStream();

        // Assert
        assertNotNull(stream);
    }

    @Test
    public void testGetCode_returnsCode() throws IOException {
        // Arrange
        Response response = createResponseWithCode(200);

        // Act
        int code = response.getCode();

        // Assert
        assertEquals(200, code);
    }

    @Test
    public void testGetHeaders_returnsHeaders() throws IOException {
        // Arrange
        Response response = createResponseWithHeaders();

        // Act
        Map<String, String> headers = response.getHeaders();

        // Assert
        assertNotNull(headers);
        assertFalse(headers.isEmpty());
    }

    @Test
    public void testGetHeader_givenExistingHeader_returnsHeaderValue() throws IOException {
        // Arrange
        Response response = createResponseWithHeaders();

        // Act
        String value = response.getHeader("Header1");

        // Assert
        assertEquals("Value1", value);
    }

    @Test
    public void testGetHeader_givenNonExistingHeader_returnsNull() throws IOException {
        // Arrange
        Response response = createResponseWithHeaders();

        // Act
        String value = response.getHeader("NonExistingHeader");

        // Assert
        assertNull(value);
    }

    private HttpURLConnection createMockConnection(int responseCode) throws IOException {
        HttpURLConnection connection = createMockConnection(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(responseCode);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        return connection;
    }

    private HttpURLConnection createMockConnection(Class<? extends Exception> exceptionClass) throws IOException {
        HttpURLConnection connection = createMockConnection(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(exceptionClass);
        return connection;
    }

    private Response createResponseWithNullBody() throws IOException {
        HttpURLConnection mockConnection = createMockConnection(200);
        Response response = new Response(mockConnection);
        response.body = null;
        return response;
    }

    private Response createResponseWithNonNullBody() throws IOException {
        HttpURLConnection mockConnection = createMockConnection(200);
        Response response = new Response(mockConnection);
        response.body = "Test Body";
        return response;
    }

    private Response createResponseWithNonNullStream() throws IOException {
        HttpURLConnection mockConnection = createMockConnection(200);
        Response response = new Response(mockConnection);
        response.stream = new ByteArrayInputStream("Test Stream".getBytes());
        return response;
    }

    private Response createResponseWithCode(int code) throws IOException {
        HttpURLConnection mockConnection = createMockConnection(code);
        return new Response(mockConnection);
    }

    private Response createResponseWithHeaders() throws IOException {
        HttpURLConnection mockConnection = createMockConnection(200);
        Response response = new Response(mockConnection);
        response.headers = new HashMap<>();
        response.headers.put("Header1", "Value1");
        response.headers.put("Header2", "Value2");
        return response;
    }
}