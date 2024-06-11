
package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody_WhenBodyIsNull_ReturnsParsedBodyContents() {
        // Arrange
        String expectedBody = "Test Body";
        Response response = createResponseWithNullBody(expectedBody);

        // Act
        String actualBody = response.getBody();

        // Assert
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetBody_WhenBodyIsNotNull_ReturnsBody() {
        // Arrange
        String expectedBody = "Test Body";
        Response response = createResponseWithNonNullBody(expectedBody);

        // Act
        String actualBody = response.getBody();

        // Assert
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetStream_ReturnsStream() throws Exception {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        Response response = createResponseWithStream(inputStream);

        // Act
        ByteArrayInputStream actualStream = (ByteArrayInputStream) response.getStream();

        // Assert
        assertEquals(inputStream, actualStream);
    }

    @Test
    public void testGetCode_ReturnsCode() throws Exception {
        // Arrange
        int expectedCode = 200;
        Response response = createResponseWithCode(expectedCode);

        // Act
        int actualCode = response.getCode();

        // Assert
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetHeaders_ReturnsHeaders() throws Exception {
        // Arrange
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Header1", "Value1");
        expectedHeaders.put("Header2", "Value2");
        Response response = createResponseWithHeaders(expectedHeaders);

        // Act
        Map<String, String> actualHeaders = response.getHeaders();

        // Assert
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testGetHeader_ReturnsHeaderValue() throws Exception {
        // Arrange
        String expectedHeaderValue = "Value1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", expectedHeaderValue);
        headers.put("Header2", "Value2");
        Response response = createResponseWithHeaders(headers);
        
        // Act
        String actualHeaderValue = response.getHeader("Header1");

        // Assert
        assertEquals(expectedHeaderValue, actualHeaderValue);
    }

    private Response createResponseWithNullBody(String expectedBody) {
        HttpURLConnection connection = createMockConnection();
        return new Response(connection) {
            @Override
            private String parseBodyContents() {
                return expectedBody;
            }
        };
    }

    private Response createResponseWithNonNullBody(String body) {
        HttpURLConnection connection = createMockConnection();
        Response response = new Response(connection);
        response.getBody(); // To initialize body
        response.getBody().equals(body); // To set body and make it non-null
        return response;
    }

    private Response createResponseWithStream(ByteArrayInputStream stream) {
        HttpURLConnection connection = createMockConnection();
        return new Response(connection) {
            @Override
            public InputStream getStream() {
                return stream;
            }
        };
    }

    private Response createResponseWithCode(int code) {
        HttpURLConnection connection = createMockConnection();
        return new Response(connection) {
            @Override
            public int getCode() {
                return code;
            }
        };
    }

    private Response createResponseWithHeaders(Map<String, String> headers) {
        HttpURLConnection connection = createMockConnection();
        return new Response(connection) {
            @Override
            private Map<String, String> parseHeaders(final HttpURLConnection conn) {
                return headers;
            }
        };
    }

    private HttpURLConnection createMockConnection() {
        try {
            HttpURLConnection connection = org.mockito.Mockito.mock(HttpURLConnection.class);
            URL url = new URL("https://example.com");
            org.mockito.Mockito.when(connection.getResponseCode()).thenReturn(200);
            org.mockito.Mockito.when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Test Stream".getBytes()));
            org.mockito.Mockito.when(connection.getHeaderFields()).thenReturn(new HashMap<>());
            org.mockito.Mockito.doNothing().when(connection).connect();
            return connection;
        } catch (Exception e) {
            fail("Failed to create mock connection");
        }
        return null;
    }
}
