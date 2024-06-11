import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

   // Test case for constructor
   @Test
   public void testConstructor() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      assertEquals(1234567890, request.statusId);
      assertEquals("https://example.com", request.url);
      assertTrue(request.hideMedia);
      assertTrue(request.hideThread);
      assertFalse(request.omitScript);
      assertEquals(OEmbedRequest.Align.NONE, request.align);
      assertArrayEquals(new String[0], request.related);
      assertNull(request.lang);
   }
   
   // Test case for setMaxWidth method
   @Test
   public void testSetMaxWidth() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setMaxWidth(800);
      assertEquals(800, request.maxWidth);
   }
   
   // Test case for MaxWidth method
   @Test
   public void testMaxWidth() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest updatedRequest = request.MaxWidth(800);
      assertEquals(800, updatedRequest.maxWidth);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for setHideMedia method
   @Test
   public void testSetHideMedia() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setHideMedia(false);
      assertFalse(request.hideMedia);
   }
   
   // Test case for HideMedia method
   @Test
   public void testHideMedia() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest updatedRequest = request.HideMedia(false);
      assertFalse(updatedRequest.hideMedia);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for setHideThread method
   @Test
   public void testSetHideThread() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setHideThread(false);
      assertFalse(request.hideThread);
   }
   
   // Test case for HideThread method
   @Test
   public void testHideThread() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest updatedRequest = request.HideThread(false);
      assertFalse(updatedRequest.hideThread);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for setOmitScript method
   @Test
   public void testSetOmitScript() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setOmitScript(true);
      assertTrue(request.omitScript);
   }
   
   // Test case for omitScript method
   @Test
   public void testOmitScript() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest updatedRequest = request.omitScript(true);
      assertTrue(updatedRequest.omitScript);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for setAlign method
   @Test
   public void testSetAlign() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setAlign(OEmbedRequest.Align.CENTER);
      assertEquals(OEmbedRequest.Align.CENTER, request.align);
   }
   
   // Test case for align method
   @Test
   public void testAlign() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.CENTER);
      assertEquals(OEmbedRequest.Align.CENTER, updatedRequest.align);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for setRelated method
   @Test
   public void testSetRelated() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      String[] related = {"user1", "user2"};
      request.setRelated(related);
      assertArrayEquals(related, request.related);
   }
   
   // Test case for related method
   @Test
   public void testRelated() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      String[] related = {"user1", "user2"};
      OEmbedRequest updatedRequest = request.related(related);
      assertArrayEquals(related, updatedRequest.related);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for setLang method
   @Test
   public void testSetLang() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setLang("en");
      assertEquals("en", request.lang);
   }
   
   // Test case for lang method
   @Test
   public void testLang() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest updatedRequest = request.lang("en");
      assertEquals("en", updatedRequest.lang);
      assertEquals(request, updatedRequest);
   }
   
   // Test case for asHttpParameterArray method
   @Test
   public void testAsHttpParameterArray() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setMaxWidth(800);
      request.setHideMedia(false);
      request.setLang("en");
      HttpParameter[] expectedParams = {
         new HttpParameter("id", "1234567890"),
         new HttpParameter("url", "https://example.com"),
         new HttpParameter("maxwidth", 800),
         new HttpParameter("hide_media", false),
         new HttpParameter("hide_thread", true),
         new HttpParameter("omit_script", false),
         new HttpParameter("align", OEmbedRequest.Align.NONE.name().toLowerCase()),
         new HttpParameter("lang", "en")
      };
      assertArrayEquals(expectedParams, request.asHttpParameterArray());
   }
   
   // Test case for equals method
   @Test
   public void testEquals() {
      OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest request3 = new OEmbedRequest(9876543210, "https://example.com");
      assertTrue(request1.equals(request2));
      assertFalse(request1.equals(request3));
      assertFalse(request1.equals(null));
      assertFalse(request1.equals("https://example.com"));
   }
   
   // Test case for hashCode method
   @Test
   public void testHashCode() {
      OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://example.com");
      OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://example.com");
      assertEquals(request1.hashCode(), request2.hashCode());
   }
   
   // Test case for toString method
   @Test
   public void testToString() {
      OEmbedRequest request = new OEmbedRequest(1234567890, "https://example.com");
      request.setMaxWidth(800);
      request.setHideMedia(false);
      request.setAlign(OEmbedRequest.Align.CENTER);
      request.setRelated(new String[]{"user1", "user2"});
      request.setLang("en");
      String expectedString = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=800, hideMedia=false, hideThread=true, omitScript=false, align=center, related=[user1, user2], lang='en'}";
      assertEquals(expectedString, request.toString());
   }
}