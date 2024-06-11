import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AccessEventTest {

	private AccessEvent accessEvent;
	
	@Before
	public void setUp() throws Exception {
		accessEvent = new AccessEvent();
	}

	@Test
	public void testGetTimeStamp() {
		Long timeStamp = 123456789L;
		accessEvent.setTimeStamp(timeStamp);
		assertEquals(timeStamp, accessEvent.getTimeStamp());
	}
	
	@Test
	public void testGetLoggerContext() {
		LoggerContext loggerContext = new LoggerContext();
		accessEvent.setLoggerContext(loggerContext);
		assertEquals(loggerContext, accessEvent.getLoggerContext());
	}
	
	@Test
	public void testGetRequestURI() {
		String requestURI = "/example";
		accessEvent.setRequestURI(requestURI);
		assertEquals(requestURI, accessEvent.getRequestURI());
	}
	
	@Test
	public void testGetRequestURL() {
		String requestURL = "http://www.example.com/example";
		accessEvent.setRequestURL(requestURL);
		assertEquals(requestURL, accessEvent.getRequestURL());
	}
	
	@Test
	public void testGetRemoteHost() {
		String remoteHost = "127.0.0.1";
		accessEvent.setRemoteHost(remoteHost);
		assertEquals(remoteHost, accessEvent.getRemoteHost());
	}
	
	@Test
	public void testGetRemoteUser() {
		String remoteUser = "user";
		accessEvent.setRemoteUser(remoteUser);
		assertEquals(remoteUser, accessEvent.getRemoteUser());
	}
	
	@Test
	public void testGetProtocol() {
		String protocol = "HTTP/1.1";
		accessEvent.setProtocol(protocol);
		assertEquals(protocol, accessEvent.getProtocol());
	}
	
	@Test
	public void testGetMethod() {
		String method = "GET";
		accessEvent.setMethod(method);
		assertEquals(method, accessEvent.getMethod());
	}
	
	@Test
	public void testGetServerName() {
		String serverName = "localhost";
		accessEvent.setServerName(serverName);
		assertEquals(serverName, accessEvent.getServerName());
	}
	
	@Test
	public void testGetRemoteAddress() {
		String remoteAddress = "127.0.0.1";
		accessEvent.setRemoteAddress(remoteAddress);
		assertEquals(remoteAddress, accessEvent.getRemoteAddress());
	}
	
	@Test
	public void testGetRequestHeaders() {
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Header1", "Value1");
		requestHeaders.put("Header2", "Value2");
		accessEvent.setRequestHeaders(requestHeaders);
		assertEquals(requestHeaders, accessEvent.getRequestHeaders());
	}
	
	@Test
	public void testGetResponseHeaders() {
		Map<String, String> responseHeaders = new HashMap<>();
		responseHeaders.put("Header1", "Value1");
		responseHeaders.put("Header2", "Value2");
		accessEvent.setResponseHeaders(responseHeaders);
		assertEquals(responseHeaders, accessEvent.getResponseHeaders());
	}
	
	@Test
	public void testGetRequestParameters() {
		Map<String, String[]> requestParams = new HashMap<>();
		requestParams.put("Param1", new String[] {"Value1"});
		requestParams.put("Param2", new String[] {"Value2"});
		accessEvent.setRequestParameters(requestParams);
		assertEquals(requestParams, accessEvent.getRequestParameters());
	}
	
	@Test
	public void testGetLocalPort() {
		int localPort = 8080;
		accessEvent.setLocalPort(localPort);
		assertEquals(localPort, accessEvent.getLocalPort());
	}
	
	@Test
	public void testGetStatusCode() {
		int statusCode = 200;
		accessEvent.setStatusCode(statusCode);
		assertEquals(statusCode, accessEvent.getStatusCode());
	}
	
	@Test
	public void testEquals() {
		AccessEvent event1 = new AccessEvent();
		AccessEvent event2 = new AccessEvent();
		
		// Test when all attributes are equal
		assertTrue(event1.equals(event2));
		
		// Test when some attributes are not equal
		event2.setStatusCode(404);
		assertFalse(event1.equals(event2));
	}
	
	@Test
	public void testHashCode() {
		AccessEvent event1 = new AccessEvent();
		AccessEvent event2 = new AccessEvent();
		
		// Test when all attributes are equal
		assertEquals(event1.hashCode(), event2.hashCode());
		
		// Test when some attributes are not equal
		event2.setStatusCode(404);
		assertNotEquals(event1.hashCode(), event2.hashCode());
	}
	
	@Test
	public void testToString() {
		String loggerContext = "TestLoggerContext";
		Long timeStamp = 123456789L;
		
		accessEvent.setLoggerContext(loggerContext);
		accessEvent.setTimeStamp(timeStamp);
		
		StringBuilder expected = new StringBuilder();
		expected.append("AccessEvent[");
		expected.append("loggerContext=").append(loggerContext).append(", ");
		expected.append("timeStamp=").append(timeStamp);
		expected.append("]");
		
		assertEquals(expected.toString(), accessEvent.toString());
	}
}