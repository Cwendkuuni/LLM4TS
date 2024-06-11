import org.junit.*;
import java.util.*;

public class AccessEventTest {
 
   private AccessEvent accessEvent;
   private LoggerContext loggerContext;
   private Map<String, String> requestHeaders;
   private Map<String, String> responseHeaders;
   private Map<String, String[]> requestParameters;

   @Before
   public void setup() {
      accessEvent = new AccessEvent();
      loggerContext = new LoggerContext();
      requestHeaders = new HashMap<>();
      responseHeaders = new HashMap<>();
      requestParameters = new HashMap<>();
   }

   @Test
   public void testGetTimeStamp() {
      accessEvent.setTimeStamp(1234567890L);
      Assert.assertEquals(1234567890L, accessEvent.getTimeStamp().longValue());
   }

    @Test
   public void testGetLoggerContext() {
      accessEvent.setLoggerContext(loggerContext);
      Assert.assertEquals(loggerContext, accessEvent.getLoggerContext());
   }

   @Test
   public void testGetRequestURI() {
      accessEvent.setRequestURI("/example");
      Assert.assertEquals("/example", accessEvent.getRequestURI());
   }

   @Test
   public void testGetRequestURL() {
      accessEvent.setRequestURL("http://example.com");
      Assert.assertEquals("http://example.com", accessEvent.getRequestURL());
   }

   @Test
   public void testGetRemoteHost() {
      accessEvent.setRemoteHost("localhost");
      Assert.assertEquals("localhost", accessEvent.getRemoteHost());
   }

   @Test
   public void testGetRemoteUser() {
      accessEvent.setRemoteUser("user");
      Assert.assertEquals("user", accessEvent.getRemoteUser());
   }

   @Test
   public void testGetProtocol() {
      accessEvent.setProtocol("HTTP/1.1");
      Assert.assertEquals("HTTP/1.1", accessEvent.getProtocol());
   }

   @Test
   public void testGetMethod() {
      accessEvent.setMethod("GET");
      Assert.assertEquals("GET", accessEvent.getMethod());
   }

   @Test
   public void testGetServerName() {
      accessEvent.setServerName("example.com");
      Assert.assertEquals("example.com", accessEvent.getServerName());
   }

   @Test
   public void testGetRemoteAddress() {
      accessEvent.setRemoteAddress("127.0.0.1");
      Assert.assertEquals("127.0.0.1", accessEvent.getRemoteAddress());
   }

   @Test
   public void testGetRequestHeaders() {
      accessEvent.setRequestHeaders(requestHeaders);
      Assert.assertEquals(requestHeaders, accessEvent.getRequestHeaders());
   }

   @Test
   public void testGetResponseHeaders() {
      accessEvent.setResponseHeaders(responseHeaders);
      Assert.assertEquals(responseHeaders, accessEvent.getResponseHeaders());
   }

   @Test
   public void testGetRequestParameters() {
      accessEvent.setRequestParameters(requestParameters);
      Assert.assertEquals(requestParameters, accessEvent.getRequestParameters());
   }

   @Test
   public void testGetLocalPort() {
      accessEvent.setLocalPort(8080);
      Assert.assertEquals(8080, accessEvent.getLocalPort());
   }

   @Test
   public void testGetStatusCode() {
      accessEvent.setStatusCode(200);
      Assert.assertEquals(200, accessEvent.getStatusCode());
   }
}

