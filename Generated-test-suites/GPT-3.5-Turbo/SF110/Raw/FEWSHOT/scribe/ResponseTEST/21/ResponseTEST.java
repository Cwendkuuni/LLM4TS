package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private Response response;
    private HttpURLConnection mockConnection;

    @Before
    public void setUp() {
        mockConnection = new MockHttpURLConnection();
    }

    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        response = new Response(mockConnection);
        assertNotNull(response.getStream());
        assertEquals(200, response.getCode());
        assertEquals("Mock body", response.getBody());
        assertEquals("MockValue1", response.getHeader("MockHeader1"));
        assertEquals("MockValue2", response.getHeader("MockHeader2"));
    }

    @Test
    public void testConstructor_ConnectionError() throws IOException {
        ((MockHttpURLConnection)mockConnection).setIsError(true);
        response = new Response(mockConnection);
        assertNull(response.getStream());
        assertEquals(403, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getHeader("MockHeader1"));
        assertNull(response.getHeader("MockHeader2"));
    }

    @Test
    public void testConstructor_UnknownHostException() throws IOException {
        ((MockHttpURLConnection)mockConnection).setIsUnknownHostException(true);
        response = new Response(mockConnection);
        assertNull(response.getStream());
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getHeader("MockHeader1"));
        assertNull(response.getHeader("MockHeader2"));
    }

    @Test
    public void testGetBody() {
        response = new Response(mockConnection);
        assertEquals("Mock body", response.getBody());
    }

    @Test
    public void testGetStream() {
        response = new Response(mockConnection);
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        response = new Response(mockConnection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        response = new Response(mockConnection);
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("MockHeader1", "MockValue1");
        expectedHeaders.put("MockHeader2", "MockValue2");

        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        response = new Response(mockConnection);
        assertEquals("MockValue1", response.getHeader("MockHeader1"));
        assertEquals("MockValue2", response.getHeader("MockHeader2"));
    }

    private class MockHttpURLConnection extends HttpURLConnection {
        private boolean isError;
        private boolean isUnknownHostException;

        public MockHttpURLConnection() {
            super(null);
            isError = false;
            isUnknownHostException = false;
        }

        public void setIsError(boolean isError) {
            this.isError = isError;
        }

        public void setIsUnknownHostException(boolean isUnknownHostException) {
            this.isUnknownHostException = isUnknownHostException;
        }

        @Override
        public void connect() throws IOException {
            // Mock connection
        }

        @Override
        public int getResponseCode() throws IOException {
            if (isError) {
                return 403;
            } else if (isUnknownHostException) {
                throw new UnknownHostException();
            } else {
                return 200;
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream("Mock body".getBytes());
        }

        @Override
        public InputStream getErrorStream() {
            if (isError) {
                return new ByteArrayInputStream("Error body".getBytes());
            }
            return null;
        }

        @Override
        public Map<String, String> getHeaderFields() {
            Map<String, String> headers = new HashMap<>();
            headers.put("MockHeader1", "MockValue1");
            headers.put("MockHeader2", "MockValue2");
            return headers;
        }
    }
}