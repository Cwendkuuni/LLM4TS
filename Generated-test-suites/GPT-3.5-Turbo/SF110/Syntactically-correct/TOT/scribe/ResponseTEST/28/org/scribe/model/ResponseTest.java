import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

    private Response response;
    private HttpURLConnection connection;

    @Before
    public void setup() {
        connection = new HttpURLConnectionStub();
        try {
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBody_NullBody_ReturnsParsedBodyContents() {
        String expected = "Test body";
        response = new Response(connection);
        response.getStream().read(expected.getBytes(), 0, expected.getBytes().length);
        assertEquals(expected, response.getBody());
    }

    @Test
    public void testGetBody_NonNullBody_ReturnsBody() {
        String expected = "Test body";
        response = new Response(connection);
        response.getStream().read(expected.getBytes(), 0, expected.getBytes().length);
        response.getBody(); // Invoke parseBodyContents() once
        assertEquals(expected, response.getBody());
    }

    @Test
    public void testGetStream_ReturnsInputStream() {
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode_ReturnsResponseCode() {
        int expected = 200;
        assertEquals(expected, response.getCode());
    }

    @Test
    public void testGetHeaders_ReturnsHeadersMap() {
        Map<String, String> expected = new HashMap<>();
        expected.put("Content-Type", "application/json");
        expected.put("Cache-Control", "no-cache");
        assertEquals(expected, response.getHeaders());
    }

    @Test
    public void testGetHeader_ValidHeaderName_ReturnsHeaderValue() {
        String expected = "application/json";
        assertEquals(expected, response.getHeader("Content-Type"));
    }

    @Test
    public void testGetHeader_InvalidHeaderName_ReturnsNull() {
        assertNull(response.getHeader("Invalid-Header"));
    }

    @Test
    public void testParseBodyContents_ValidBody_ReturnsParsedBody() {
        String expected = "Test body";
        response = new Response(connection);
        response.getStream().read(expected.getBytes(), 0, expected.getBytes().length);
        assertEquals(expected, response.parseBodyContents());
    }

    @Test
    public void testParseHeaders_ValidConnection_ReturnsHeadersMap() {
        Map<String, String> expected = new HashMap<>();
        expected.put("Content-Type", "application/json");
        expected.put("Cache-Control", "no-cache");
        assertEquals(expected, response.parseHeaders(connection));
    }

    @Test
    public void testWasSuccessful_SuccessfulResponse_ReturnsTrue() {
        response = new Response(connection);
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessful_UnsuccessfulResponse_ReturnsFalse() {
        connection = new HttpURLConnectionStub(404);
        response = new Response(connection);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessful_UnexpectedResponseCode_ReturnsFalse() {
        connection = new HttpURLConnectionStub(500);
        response = new Response(connection);
        assertFalse(response.wasSuccessful());
    }

    private class HttpURLConnectionStub extends HttpURLConnection {

        private int responseCode;
        private Map<String, String> headerFields;

        public HttpURLConnectionStub() {
            super(null);
        }

        public HttpURLConnectionStub(int responseCode) {
            super(null);
            this.responseCode = responseCode;
        }

        @Override
        public int getResponseCode() throws IOException {
            return responseCode;
        }

        @Override
        public Map<String, java.util.List<String>> getHeaderFields() {
            Map<String, java.util.List<String>> headers = new HashMap<>();
            headers.put("Content-Type", Arrays.asList("application/json"));
            headers.put("Cache-Control", Arrays.asList("no-cache"));
            return headers;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new InputStreamStub();
        }

        @Override
        public InputStream getErrorStream() {
            return new InputStreamStub();
        }

        private class InputStreamStub extends InputStream {

            private byte[] data = new byte[1024];
            private int currentIndex = 0;

            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int bytesRead = Math.min(data.length - currentIndex, len);
                System.arraycopy(data, currentIndex, b, off, bytesRead);
                currentIndex += bytesRead;
                return bytesRead;
            }
        }
    }
}