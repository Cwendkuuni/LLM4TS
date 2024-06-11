
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private Response response;

    @Before
    public void setup() {
        response = new Response(createMockConnection());
    }

    @Test
    public void testConstructor() throws IOException {
        assertEquals(200, response.getCode());
        assertEquals("mockBody", response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertEquals("headerValue", response.getHeader("headerName"));
    }

    @Test
    public void testUnknownHostExceptionConstructor() {
        Response unknownHostResponse = new Response(createUnknownHostExceptionConnection());
        assertEquals(404, unknownHostResponse.getCode());
        assertEquals("", unknownHostResponse.getBody());
        assertNull(unknownHostResponse.getStream());
        assertNotNull(unknownHostResponse.getHeaders());
    }

    @Test
    public void testGetBody() {
        assertEquals("mockBody", response.getBody());
    }

    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        assertNotNull(response.getHeaders());
        assertEquals("headerValue", response.getHeader("headerName"));
    }

    private HttpURLConnection createMockConnection() throws IOException {
        HttpURLConnection connection = createConnection();
        prepareMockResponse(connection);
        return connection;
    }

    private void prepareMockResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = new ByteArrayInputStream("mockBody".getBytes());
        connection.setInputStream(inputStream);

        Map<String, String> headers = new HashMap<>();
        headers.put("headerName", "headerValue");
        connection.setHeaderFields(headers);
    }

    private HttpURLConnection createUnknownHostExceptionConnection() {
        HttpURLConnection connection = createConnection();
        prepareUnknownHostExceptionResponse(connection);
        return connection;
    }

    private void prepareUnknownHostExceptionResponse(HttpURLConnection connection) {
        try {
            connection.getResponseCode();
        } catch (UnknownHostException e) {
            connection = null;
        }
    }

    private HttpURLConnection createConnection() {
        return new HttpURLConnectionMock();
    }

    private static class HttpURLConnectionMock extends HttpURLConnection {

        protected HttpURLConnectionMock() {
            super(null);
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }
        
        @Override
        public InputStream getErrorStream() {
            return null;
        }
        
        @Override
        public int getResponseCode() throws IOException {
            return 200;
        }
    }
}
