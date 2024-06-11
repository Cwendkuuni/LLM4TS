import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
            @Override
            public void disconnect() {}

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {}

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Example Response Body".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream("Example Error Body".getBytes());
            }
        };
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        String expectedBody = "Example Response Body";
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetStream() {
        InputStream expectedStream = new ByteArrayInputStream("Example Response Body".getBytes());
        InputStream actualStream = response.getStream();
        assertEquals(expectedStream, actualStream);
    }

    @Test
    public void testGetCode() {
        int expectedCode = 200;
        int actualCode = response.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html");
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testGetHeader() {
        String expectedHeaderValue = "text/html";
        String actualHeaderValue = response.getHeader("Content-Type");
        assertEquals(expectedHeaderValue, actualHeaderValue);
    }

    @Test
    public void testEdgeCaseEmptyBody() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
            @Override
            public void disconnect() {}

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {}

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }
        };
        Response response = new Response(connection);
        String expectedBody = "";
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testExceptionHandlingUnknownHost() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://unknownhost")) {
            @Override
            public void disconnect() {}

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {
                throw new UnknownHostException("Unknown host");
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }
        };
        Response response = new Response(connection);
        int expectedCode = 404;
        int actualCode = response.getCode();
        assertEquals(expectedCode, actualCode);
        String expectedBody = "";
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }
}

