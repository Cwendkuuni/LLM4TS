package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

private HttpURLConnection stubConnection;
private Response response;

@Before
public void setUp() {
stubConnection = new StubHttpURLConnection();
response = new Response(stubConnection);

}

@Test
public void testGetBody_WhenBodyIsNull_ReturnsParsedBodyContents() {
String expected = "Parsed Body Contents";
response.getBody();
assertEquals(expected, response.getBody());
}

@Test
public void testGetBody_WhenBodyIsNotNull_ReturnsBody() {
String expected = "Body";
response.getBody();
assertEquals(expected, response.getBody());
}

@Test
public void testGetStream_ReturnsStream() {
InputStream expected = new ByteArrayInputStream(new byte[1024]);
response.getStream();
assertEquals(expected, response.getStream());
}

@Test
public void testGetCode_ReturnsCode() {
int expected = 200;
response.getCode();
assertEquals(expected, response.getCode());
}

@Test
public void testGetHeaders_ReturnsHeaders() {
Map<String, String> expected = new HashMap<>();
expected.put("Header1", "Value1");
response.getHeaders();
assertEquals(expected, response.getHeaders());
}

@Test
public void testGetHeader_ReturnsValue() {
String expected = "Value1";
response.getHeader("Header1");
assertEquals(expected, response.getHeader("Header1"));
}

private class StubHttpURLConnection extends HttpURLConnection {

public StubHttpURLConnection() {
super(null);
}

@Override
public void connect() throws IOException {}

@Override
public int getResponseCode() throws IOException {
return 200;
}

@Override
public InputStream getInputStream() throws IOException {
return new ByteArrayInputStream(new byte[1024]);
}

@Override
public InputStream getErrorStream() {
return null;
}

@Override
public Map<String, java.util.List<String>> getHeaderFields() {
Map<String, java.util.List<String>> headers = new HashMap<>();
headers.put("Header1", java.util.List.of("Value1"));
return headers;
}
}

}