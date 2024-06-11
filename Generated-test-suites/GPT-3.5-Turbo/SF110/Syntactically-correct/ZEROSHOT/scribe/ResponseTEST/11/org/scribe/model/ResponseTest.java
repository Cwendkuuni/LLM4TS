import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResponseTest {

    private Response response;
    private HttpURLConnectionMock connectionMock;

    @Before
    public void setup() throws IOException {
        connectionMock = new HttpURLConnectionMock();
        response = new Response(connectionMock);
    }

    @Test
    public void testGetBody() {
        // Test getBody when body is already set
        response = new Response(connectionMock);
        response.getBody();
        String expectedResult = "";
        String actualResult = response.getBody();
        assertEquals(expectedResult, actualResult);

        // Test getBody when body is null
        String responseBody = "This is the response body";
        connectionMock.setInputStream(new ByteArrayInputStream(responseBody.getBytes()));
        response = new Response(connectionMock);
        expectedResult = responseBody;
        actualResult = response.getBody();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetStream() {
        InputStream expectedResult = connectionMock.getInputStream();
        InputStream actualResult = response.getStream();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetCode() {
        int expectedResult = connectionMock.getResponseCode();
        int actualResult = response.getCode();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedResult = connectionMock.getHeaderFields();
        Map<String, String> actualResult = response.getHeaders();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetHeader() {
        String headerName = "Content-Type";
        String expectedResult = connectionMock.getHeaderField(headerName);
        String actualResult = response.getHeader(headerName);
        assertEquals(expectedResult, actualResult);
    }

    // Helper class for mocking HttpURLConnection
    private class HttpURLConnectionMock extends HttpURLConnection {

        private Map<String, String> headerFields;
        private InputStream inputStream;
        private int responseCode;

        public HttpURLConnectionMock() {
            super(null);
            headerFields = new HashMap<>();
            inputStream = null;
            responseCode = 200; // Default response code is 200 OK
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> headerFieldsMap = new HashMap<>();
            for (String key : headerFields.keySet()) {
                List<String> headerValues = new ArrayList<>();
                headerValues.add(headerFields.get(key));
                headerFieldsMap.put(key, headerValues);
            }
            return headerFieldsMap;
        }

        @Override
        public String getHeaderField(String name) {
            return headerFields.get(name);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }

        @Override
        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }
    }

    // Assertion helper method
    private void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
        }
    }
}