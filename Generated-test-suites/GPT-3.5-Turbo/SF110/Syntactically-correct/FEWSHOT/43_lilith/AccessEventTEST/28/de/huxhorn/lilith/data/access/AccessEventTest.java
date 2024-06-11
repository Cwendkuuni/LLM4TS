
package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

	@Test
	public void testGetSetTimeStamp() {
		AccessEvent event = new AccessEvent();
		Long timeStamp = 1234567890L;
		event.setTimeStamp(timeStamp);
		assertEquals(timeStamp, event.getTimeStamp());
	}

	@Test
	public void testGetSetLoggerContext() {
		AccessEvent event = new AccessEvent();
		LoggerContext loggerContext = new LoggerContext();
		event.setLoggerContext(loggerContext);
		assertEquals(loggerContext, event.getLoggerContext());
	}

	@Test
	public void testGetSetRequestURI() {
		AccessEvent event = new AccessEvent();
		String requestURI = "/example";
		event.setRequestURI(requestURI);
		assertEquals(requestURI, event.getRequestURI());
	}

	@Test
	public void testGetSetRequestURL() {
		AccessEvent event = new AccessEvent();
		String requestURL = "http://example.com";
		event.setRequestURL(requestURL);
		assertEquals(requestURL, event.getRequestURL());
	}

	@Test
	public void testGetSetRemoteHost() {
		AccessEvent event = new AccessEvent();
		String remoteHost = "example.com";
		event.setRemoteHost(remoteHost);
		assertEquals(remoteHost, event.getRemoteHost());
	}

	@Test
	public void testGetSetRemoteUser() {
		AccessEvent event = new AccessEvent();
		String remoteUser = "user";
		event.setRemoteUser(remoteUser);
		assertEquals(remoteUser, event.getRemoteUser());
	}

	@Test
	public void testGetSetProtocol() {
		AccessEvent event = new AccessEvent();
		String protocol = "HTTP/1.1";
		event.setProtocol(protocol);
		assertEquals(protocol, event.getProtocol());
	}

	@Test
	public void testGetSetMethod() {
		AccessEvent event = new AccessEvent();
		String method = "GET";
		event.setMethod(method);
		assertEquals(method, event.getMethod());
	}

	@Test
	public void testGetSetServerName() {
		AccessEvent event = new AccessEvent();
		String serverName = "localhost";
		event.setServerName(serverName);
		assertEquals(serverName, event.getServerName());
	}

	@Test
	public void testGetSetRemoteAddress() {
		AccessEvent event = new AccessEvent();
		String remoteAddress = "127.0.0.1";
		event.setRemoteAddress(remoteAddress);
		assertEquals(remoteAddress, event.getRemoteAddress());
	}

	@Test
	public void testGetSetRequestHeaders() {
		AccessEvent event = new AccessEvent();
		Map<String, String> requestHeaders = new HashMap<>();
		event.setRequestHeaders(requestHeaders);
		assertEquals(requestHeaders, event.getRequestHeaders());
	}

	@Test
	public void testGetSetResponseHeaders() {
		AccessEvent event = new AccessEvent();
		Map<String, String> responseHeaders = new HashMap<>();
		event.setResponseHeaders(responseHeaders);
		assertEquals(responseHeaders, event.getResponseHeaders());
	}

	@Test
	public void testGetSetRequestParameters() {
		AccessEvent event = new AccessEvent();
		Map<String, String[]> requestParameters = new HashMap<>();
		event.setRequestParameters(requestParameters);
		assertEquals(requestParameters, event.getRequestParameters());
	}

	@Test
	public void testGetSetLocalPort() {
		AccessEvent event = new AccessEvent();
		int localPort = 8080;
		event.setLocalPort(localPort);
		assertEquals(localPort, event.getLocalPort());
	}

	@Test
	public void testGetSetStatusCode() {
		AccessEvent event = new AccessEvent();
		int statusCode = 200;
		event.setStatusCode(statusCode);
		assertEquals(statusCode, event.getStatusCode());
	}

	@Test
	public void testEquals() {
		AccessEvent event1 = new AccessEvent();
		AccessEvent event2 = new AccessEvent();
		assertEquals(event1, event2);

		event1.setTimeStamp(1234567890L);
		assertNotEquals(event1, event2);
	}

	@Test
	public void testHashCode() {
		AccessEvent event1 = new AccessEvent();
		AccessEvent event2 = new AccessEvent();
		assertEquals(event1.hashCode(), event2.hashCode());

		event1.setTimeStamp(1234567890L);
		assertNotEquals(event1.hashCode(), event2.hashCode());
	}

	@Test
	public void testToString() {
		AccessEvent event = new AccessEvent();
		LoggerContext loggerContext = new LoggerContext();
		event.setLoggerContext(loggerContext);
		event.setTimeStamp(1234567890L);

		String expected = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=1234567890]";
		assertEquals(expected, event.toString());
	}

}
