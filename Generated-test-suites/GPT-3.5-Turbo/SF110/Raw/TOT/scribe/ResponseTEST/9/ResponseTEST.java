import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ResponseTest {

    @Test
    public void testResponseConstructor_successfulConnection() throws IOException {
        // Test scenario: Successful connection
        HttpURLConnection connection = new FakeHttpURLConnection(200, null, null);
        Response response = new Response(connection);

        Assertions.assertEquals(200, response.getCode());
        Assertions.assertNull(response.getBody());
        Assertions.assertNull(response.getStream());
        Assertions.assertNotNull(response.getHeaders());
        Assertions.assertTrue(response.getHeaders().isEmpty());
    }

    @Test
    public void testResponseConstructor_successfulConnectionWithHeaders() throws IOException {
        // Test scenario: Successful connection with headers
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");

        HttpURLConnection connection = new FakeHttpURLConnection(200, null, expectedHeaders);
        Response response = new Response(connection);

        Assertions.assertEquals(200, response.getCode());
        Assertions.assertNull(response.getBody());
        Assertions.assertNull(response.getStream());
        Assertions.assertNotNull(response.getHeaders());
        Assertions.assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testResponseConstructor_unsuccessfulConnection() {
        // Test scenario: Unsuccessful connection (UnknownHostException)
        UnknownHostException exception = Assertions.assertThrows(UnknownHostException.class, () -> {
            HttpURLConnection connection = new FakeHttpURLConnection(404, null, null);
            Response response = new Response(connection);
        });

        Assertions.assertEquals("Failed to establish connection", exception.getMessage());
    }

    @Test
    public void testGetBody_existingBody() throws IOException {
        // Test scenario: Body is already initialized
        String expectedBody = "Sample body";
        Response response = new Response(new FakeHttpURLConnection(200, expectedBody, null));

        String body = response.getBody();

        Assertions.assertEquals(expectedBody, body);
    }

    @Test
    public void testGetBody_nullBody() throws IOException {
        // Test scenario: Body is null and needs to be parsed
        InputStream inputStream = new ByteArrayInputStream("Sample body".getBytes());
        Response response = new Response(new FakeHttpURLConnection(200, null, null));

        response.getStream();  // Simulate initializing the stream
        String body = response.getBody();

        Assertions.assertEquals("Sample body", body);
    }

    @Test
    public void testGetStream_existingStream() throws IOException {
        // Test scenario: Stream is already initialized
        InputStream expectedStream = new ByteArrayInputStream("Sample stream".getBytes());
        Response response = new Response(new FakeHttpURLConnection(200, null, null));
        response.stream = expectedStream;

        InputStream stream = response.getStream();

        Assertions.assertEquals(expectedStream, stream);
    }

    @Test
    public void testGetStream_nullStream() throws IOException {
        // Test scenario: Stream is null
        Response response = new Response(new FakeHttpURLConnection(200, null, null));

        InputStream stream = response.getStream();

        Assertions.assertNull(stream);
    }

    @Test
    public void testGetCode() throws IOException {
        // Test scenario: Getting the code
        int expectedCode = 200;
        Response response = new Response(new FakeHttpURLConnection(expectedCode, null, null));

        int code = response.getCode();

        Assertions.assertEquals(expectedCode, code);
    }

    @Test
    public void testGetHeaders_existingHeaders() throws IOException {
        // Test scenario: Headers are already initialized
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        Response response = new Response(new FakeHttpURLConnection(200, null, expectedHeaders));

        Map<String, String> headers = response.getHeaders();

        Assertions.assertEquals(expectedHeaders, headers);
    }

    @Test
    public void testGetHeaders_emptyHeaders() throws IOException {
        // Test scenario: Headers are empty
        Response response = new Response(new FakeHttpURLConnection(200, null, null));

        Map<String, String> headers = response.getHeaders();

        Assertions.assertNotNull(headers);
        Assertions.assertTrue(headers.isEmpty());
    }

    @Test
    public void testGetHeader_existingHeader() throws IOException {
        // Test scenario: Existing header
        String expectedHeader = "application/json";
        Response response = new Response(new FakeHttpURLConnection(200, null, null));
        response.headers.put("Content-Type", expectedHeader);

        String header = response.getHeader("Content-Type");

        Assertions.assertEquals(expectedHeader, header);
    }

    @Test
    public void testGetHeader_nonExistingHeader() throws IOException {
        // Test scenario: Non-existing header
        Response response = new Response(new FakeHttpURLConnection(200, null, null));

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
            response.getHeader("Content-Type");
        });

        Assertions.assertEquals("Header not found", exception.getMessage());
    }
}

class FakeHttpURLConnection extends HttpURLConnection {

    private final int responseCode;
    private final String responseMessage;
    private final Map<String, String> headers;
    private boolean connected = false;

    public FakeHttpURLConnection(int responseCode, String responseMessage, Map<String, String> headers) {
        super(null);  // Passing null URL to the superclass constructor
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.headers = headers;
    }

    @Override
    public void connect() throws IOException {
        connected = true;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public InputStream getErrorStream() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getResponseMessage() throws IOException {
        return responseMessage;
    }

    @Override
    public Map<String, java.util.List<String>> getHeaderFields() {
        Map<String, java.util.List<String>> headerFields = new HashMap<>();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                headerFields.put(key, java.util.Arrays.asList(value));
            }
        }
        return headerFields;
    }

    @Override
    public String getHeaderField(String name) {
        if (headers != null) {
            return headers.get(name);
        }
        return null;
    }
}