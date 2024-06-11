import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
  
  @Test
  public void testConstructor() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    assertEquals(123456789, oEmbedRequest.getStatusId());
    assertEquals("https://example.com", oEmbedRequest.getUrl());
    assertTrue(oEmbedRequest.isHideMedia());
    assertTrue(oEmbedRequest.isHideThread());
    assertFalse(oEmbedRequest.isOmitScript());
    assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.getAlign());
    assertArrayEquals(new String[0], oEmbedRequest.getRelated());
    assertNull(oEmbedRequest.getLang());
  }
  
  @Test
  public void testSetMaxWidth() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setMaxWidth(500);
    assertEquals(500, oEmbedRequest.getMaxWidth());
  }
  
  @Test
  public void testMaxWidth() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").MaxWidth(500);
    assertEquals(500, oEmbedRequest.getMaxWidth());
  }
  
  @Test
  public void testSetHideMedia() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setHideMedia(false);
    assertFalse(oEmbedRequest.isHideMedia());
  }
  
  @Test
  public void testHideMedia() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").HideMedia(false);
    assertFalse(oEmbedRequest.isHideMedia());
  }
  
  @Test
  public void testSetHideThread() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setHideThread(false);
    assertFalse(oEmbedRequest.isHideThread());
  }
  
  @Test
  public void testHideThread() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").HideThread(false);
    assertFalse(oEmbedRequest.isHideThread());
  }
  
  @Test
  public void testSetOmitScript() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setOmitScript(true);
    assertTrue(oEmbedRequest.isOmitScript());
  }
  
  @Test
  public void testOmitScript() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").omitScript(true);
    assertTrue(oEmbedRequest.isOmitScript());
  }
  
  @Test
  public void testSetAlign() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
    assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.getAlign());
  }
  
  @Test
  public void testAlign() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").align(OEmbedRequest.Align.CENTER);
    assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.getAlign());
  }
  
  @Test
  public void testSetRelated() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    String[] related = {"related1", "related2"};
    oEmbedRequest.setRelated(related);
    assertArrayEquals(related, oEmbedRequest.getRelated());
  }
  
  @Test
  public void testRelated() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").related(new String[]{"related1", "related2"});
    assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.getRelated());
  }
  
  @Test
  public void testSetLang() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setLang("en");
    assertEquals("en", oEmbedRequest.getLang());
  }
  
  @Test
  public void testLang() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").lang("en");
    assertEquals("en", oEmbedRequest.getLang());
  }
  
  @Test
  public void testAsHttpParameterArray() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    oEmbedRequest.setMaxWidth(500);
    oEmbedRequest.setHideMedia(false);
    oEmbedRequest.setHideThread(false);
    oEmbedRequest.setOmitScript(true);
    oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
    oEmbedRequest.setRelated(new String[]{"related1", "related2"});
    oEmbedRequest.setLang("en");
    HttpParameter[] expected = {
      new HttpParameter("id", "123456789"),
      new HttpParameter("url", "https://example.com"),
      new HttpParameter("maxwidth", 500),
      new HttpParameter("hide_media", false),
      new HttpParameter("hide_thread", false),
      new HttpParameter("omit_script", true),
      new HttpParameter("align", "center"),
      new HttpParameter("related", "related1,related2"),
      new HttpParameter("lang", "en")
    };
    assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
  }
  
  @Test
  public void testEquals() {
    OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com").MaxWidth(500);
    OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com").MaxWidth(500);
    assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
  }
  
  @Test
  public void testHashCode() {
    OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com").MaxWidth(500);
    OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com").MaxWidth(500);
    assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
  }
  
  @Test
  public void testToString() {
    OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com").MaxWidth(500);
    String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=500, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
    assertEquals(expected, oEmbedRequest.toString());
  }
}