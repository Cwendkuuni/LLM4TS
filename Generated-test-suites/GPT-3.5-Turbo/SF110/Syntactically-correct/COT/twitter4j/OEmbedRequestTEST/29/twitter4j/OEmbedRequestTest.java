
import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
  
  @Test
  public void testSetMaxWidth() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setMaxWidth(500);
    
    assertEquals(500, request.maxWidth);
  }
  
  @Test
  public void testHideMedia() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setHideMedia(false);
    
    assertFalse(request.hideMedia);
  }
  
  @Test
  public void testHideThread() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setHideThread(false);
    
    assertFalse(request.hideThread);
  }
  
  @Test
  public void testOmitScript() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setOmitScript(true);
    
    assertTrue(request.omitScript);
  }
  
  @Test
  public void testSetAlign() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setAlign(OEmbedRequest.Align.CENTER);
    
    assertEquals(OEmbedRequest.Align.CENTER, request.align);
  }
  
  @Test
  public void testSetRelated() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setRelated(new String[]{"related1", "related2"});
    
    assertArrayEquals(new String[]{"related1", "related2"}, request.related);
  }
  
  @Test
  public void testSetLang() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    request.setLang("en");
    
    assertEquals("en", request.lang);
  }
  
  @Test
  public void testEquals() {
    OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
    OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
    
    assertTrue(request1.equals(request2));
  }
  
  @Test
  public void testHashCode() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    int hashCode = request.hashCode();
    
    assertNotNull(hashCode);
  }
  
  @Test
  public void testToString() {
    OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
    
    assertNotNull(request.toString());
  }
  
}
