import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scribe.model.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {

    private HttpURLConnection mockConnection;
    private Response mockResponse;

    @BeforeEach
    public void setup() {
        mockConnection = new MockHttpURLConnection();
    }

    @Test
    public void testResponseConstructor_SuccessfulConnection_StreamIsNotNull() throws IOException {
        mockConnection.setResponseCode(200);
        mockResponse = new Response(mockConnection);

        assertNotNull(mockResponse.getStream(), "Stream should not be null");
    }

    @Test
    public void testResponseConstructor_FailedConnection_StreamIsNull() throws IOException {
        mockConnection.setResponseCode(500);
        mockResponse = new Response(mockConnection);

        assertNull(mockResponse.getStream(), "Stream should be null");
    }

    @Test
    public void testGetBody_InitializeBodyUsingParseBodyContents_SuccessfullyInitialized() {
        mockResponse = createMockResponseWithBody("Test Body");
        assertEquals("Test Body", mockResponse.getBody(), "getBody should return initialized body");
    }

    @Test
    public void testGetBody_InitializeBodyUsingParseBodyContents_EmptyBody() {
        mockResponse = createMockResponseWithBody("");
        assertEquals("", mockResponse.getBody(), "getBody should return empty body");
    }

    @Test
    public void testGetBody_GetStreamThrowsIOException_ReturnsEmptyBody() throws IOException {
        InputStream errorStream = new ByteArrayInputStream("Error Stream".getBytes());
        mockResponse = createMockResponseWithInputStream(errorStream);
        mockResponse.getStream().close();

        assertEquals("", mockResponse.getBody(), "getBody should return empty body when stream is closed");
    }

    @Test
    public void testGetStream_SuccessfulConnection_ReturnsInputStream() throws IOException {
        mockResponse = createMockResponseWithBody("Test Body");
        assertNotNull(mockResponse.getStream(), "getStream should return an InputStream");
    }

    @Test
    public void testGetCode_SuccessfulConnection_ReturnsStatusCode() throws IOException {
        mockConnection.setResponseCode(200);
        mockResponse = new Response(mockConnection);

        assertEquals(200, mockResponse.getCode(), "getCode should return correct status code");
    }

    @Test
    public void testGetHeaders_SuccessfulConnection_ReturnsHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        expectedHeaders.put("Pragma", "no-cache");

        mockConnection.setRequestProperty("Content-Type", "application/json");
        mockConnection.setRequestProperty("Cache-Control", "no-cache");
        mockConnection.setRequestProperty("Pragma", "no-cache");
        mockResponse = new Response(mockConnection);

        assertEquals(expectedHeaders, mockResponse.getHeaders(), "getHeaders should return correct headers");
    }

    @Test
    public void testGetHeader_HeaderExists_ReturnsHeaderValue() throws IOException {
        mockConnection.setRequestProperty("Content-Type", "application/json");
        mockResponse = new Response(mockConnection);

        assertEquals("application/json", mockResponse.getHeader("Content-Type"), "getHeader should return correct header value");
    }

    @Test
    public void testGetHeader_HeaderDoesNotExist_ReturnsNull() throws IOException {
        mockResponse = new Response(mockConnection);

        assertNull(mockResponse.getHeader("Invalid-Header"), "getHeader should return null when header does not exist");
    }

    private Response createMockResponseWithBody(String body) {
        try {
            mockConnection.connect();
            mockConnection.setResponseCode(200);
            mockConnection.setInputStream(new ByteArrayInputStream(body.getBytes()));
            return new Response(mockConnection);
        } catch (IOException e) {
            return null;
        }
    }

    private Response createMockResponseWithInputStream(InputStream inputStream) {
        try {
            mockConnection.connect();
            mockConnection.setResponseCode(200);
            mockConnection.setInputStream(inputStream);
            return new Response(mockConnection);
        } catch (IOException e) {
            return null;
        }
    }
}