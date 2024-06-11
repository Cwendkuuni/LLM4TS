package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {

    @InjectMocks
    private Response response;

    @Mock
    private HttpURLConnection connection;

    @Before
    public void setUp() throws IOException {
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        Mockito.when(connection.getInputStream()).thenReturn(getMockInputStream());
        Mockito.when(connection.getErrorStream()).thenReturn(getMockInputStream());
        Mockito.when(connection.getHeaderFields()).thenReturn(getMockHeaderFields());
    }

    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
        assertNotNull(response.getStream());
        assertNotNull(response.getBody());
    }

    @Test
    public void testConstructor_UnknownHostException() throws IOException {
        Mockito.when(connection.getResponseCode()).thenThrow(new UnknownHostException());
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertTrue(response.getHeaders().isEmpty());
        assertNull(response.getStream());
        assertEquals("", response.getBody());
    }

    @Test
    public void testGetBody_WithExistingBody() {
        response.setBody("Sample Body");

        assertEquals("Sample Body", response.getBody());
    }

    @Test
    public void testGetBody_WithoutExistingBody() throws IOException {
        InputStream inputStream = getMockInputStream();
        Mockito.when(inputStream.read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(-1);

        Response response = new Response(connection);

        assertEquals("", response.getBody());
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
        assertEquals("value1", response.getHeaders().get("header1"));
        assertEquals("value2", response.getHeaders().get("header2"));
    }

    @Test
    public void testGetHeader() {
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
    }

    private InputStream getMockInputStream() {
        return Mockito.mock(InputStream.class);
    }

    private Map<String, java.util.List<String>> getMockHeaderFields() {
        Map<String, java.util.List<String>> headerFields = new HashMap<>();
        headerFields.put("header1", java.util.Arrays.asList("value1"));
        headerFields.put("header2", java.util.Arrays.asList("value2"));
        return headerFields;
    }
}