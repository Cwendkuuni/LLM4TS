import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;


public class AccessEventTest {

   private AccessEvent accessEvent;
   
   @Before
   public void setUp() {
      accessEvent = new AccessEvent();
   }

   @Test
   public void testGetTimeStamp() {
      Long timeStamp = 1234567890L;
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
      String requestURL = "https://example.com";
      accessEvent.setRequestURL(requestURL);
      assertEquals(requestURL, accessEvent.getRequestURL());
   }

   @Test
   public void testGetRemoteHost() {
      String remoteHost = "example.com";
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
      String serverName = "example.server";
      accessEvent.setServerName(serverName);
      assertEquals(serverName, accessEvent.getServerName());
   }

   @Test
   public void testGetRemoteAddress() {
      String remoteAddress = "192.168.0.1";
      accessEvent.setRemoteAddress(remoteAddress);
      assertEquals(remoteAddress, accessEvent.getRemoteAddress());
   }

   @Test
   public void testGetRequestHeaders() {
      Map<String, String> requestHeaders = new HashMap<>();
      requestHeaders.put("header1", "value1");
      requestHeaders.put("header2", "value2");
      accessEvent.setRequestHeaders(requestHeaders);
      assertEquals(requestHeaders, accessEvent.getRequestHeaders());
   }

   @Test
   public void testGetResponseHeaders() {
      Map<String, String> responseHeaders = new HashMap<>();
      responseHeaders.put("header1", "value1");
      responseHeaders.put("header2", "value2");
      accessEvent.setResponseHeaders(responseHeaders);
      assertEquals(responseHeaders, accessEvent.getResponseHeaders());
   }

   @Test
   public void testGetRequestParameters() {
      Map<String, String[]> requestParameters = new HashMap<>();
      requestParameters.put("param1", new String[]{"value1"});
      requestParameters.put("param2", new String[]{"value2", "value3"});
      accessEvent.setRequestParameters(requestParameters);
      assertEquals(requestParameters, accessEvent.getRequestParameters());
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
      assertEquals(event1, event2);
   }

   @Test
   public void testHashCode() {
      AccessEvent event1 = new AccessEvent();
      AccessEvent event2 = new AccessEvent();
      assertEquals(event1.hashCode(), event2.hashCode());
   }

   @Test
   public void testToString() {
      AccessEvent event = new AccessEvent();
      String expected = "AccessEvent[loggerContext=null, timeStamp=null]";
      assertEquals(expected, event.toString());
   }
}