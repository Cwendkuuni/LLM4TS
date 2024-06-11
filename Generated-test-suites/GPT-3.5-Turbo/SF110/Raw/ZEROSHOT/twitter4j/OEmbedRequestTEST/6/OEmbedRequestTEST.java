import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class OEmbedRequestTest {
  
  @Test
  public void testMaxWidth() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    request.setMaxWidth(400);
    assertEquals(400, request.maxWidth);
  }
  
  @Test
  public void testHideMedia() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    request.setHideMedia(false);
    assertFalse(request.hideMedia);
  }
  
  @Test
  public void testHideThread() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    request.setHideThread(false);
    assertFalse(request.hideThread);
  }
  
  @Test
  public void testOmitScript() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    request.setOmitScript(true);
    assertTrue(request.omitScript);
  }
  
  @Test
  public void testAlign() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    request.setAlign(OEmbedRequest.Align.RIGHT);
    assertEquals(OEmbedRequest.Align.RIGHT, request.align);
  }
  
  @Test
  public void testRelated() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    String[] related = {"friend1", "friend2"};
    request.setRelated(related);
    assertArrayEquals(related, request.related);
  }
  
  @Test
  public void testLang() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    request.setLang("en");
    assertEquals("en", request.lang);
  }
  
  @Test
  public void testAsHttpParameterArray() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    String[] related = {"friend1", "friend2"};
    request.setRelated(related);
    request.setLang("en");
    request.setAlign(OEmbedRequest.Align.LEFT);
    request.setOmitScript(true);
    request.setHideThread(false);
    request.setHideMedia(false);
    request.setMaxWidth(500);
    
    HttpParameter[] expectedParams = {
      new HttpParameter("id", "123"),
      new HttpParameter("url", "https://example.com"),
      new HttpParameter("maxwidth", 500),
      new HttpParameter("hide_media", false),
      new HttpParameter("hide_thread", false),
      new HttpParameter("omit_script", true),
      new HttpParameter("align", "left"),
      new HttpParameter("related", "friend1,friend2"),
      new HttpParameter("lang", "en")
    };
    
    assertArrayEquals(expectedParams, request.asHttpParameterArray());
  }
  
  @Test
  public void testEquals() {
    OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
    OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
    assertTrue(request1.equals(request2));
  }
  
  @Test
  public void testHashCode() {
    OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
    OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
    assertEquals(request1.hashCode(), request2.hashCode());
  }
  
  @Test
  public void testToString() {
    OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
    String expectedString = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
    assertEquals(expectedString, request.toString());
  }
}