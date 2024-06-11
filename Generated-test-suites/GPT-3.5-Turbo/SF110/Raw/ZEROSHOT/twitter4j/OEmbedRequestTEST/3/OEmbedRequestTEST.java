package twitter4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
  
  private OEmbedRequest oEmbedRequest;
  private long statusId;
  private String url;
  
  @Before
  public void setUp() {
    statusId = 1234;
    url = "https://example.com";
    oEmbedRequest = new OEmbedRequest(statusId, url);
  }
  
  @Test
  public void testSetMaxWidth() {
    int maxWidth = 500;
    oEmbedRequest.setMaxWidth(maxWidth);
    
    assertEquals(maxWidth, oEmbedRequest.maxWidth);
  }
  
  @Test
  public void testMaxWidth() {
    int maxWidth = 500;
    OEmbedRequest result = oEmbedRequest.MaxWidth(maxWidth);
    
    assertEquals(maxWidth, result.maxWidth);
  }
  
  @Test
  public void testSetHideMedia() {
    boolean hideMedia = false;
    oEmbedRequest.setHideMedia(hideMedia);
    
    assertEquals(hideMedia, oEmbedRequest.hideMedia);
  }
  
  @Test
  public void testHideMedia() {
    boolean hideMedia = false;
    OEmbedRequest result = oEmbedRequest.HideMedia(hideMedia);
    
    assertEquals(hideMedia, result.hideMedia);
  }
  
  @Test
  public void testSetHideThread() {
    boolean hideThread = false;
    oEmbedRequest.setHideThread(hideThread);
    
    assertEquals(hideThread, oEmbedRequest.hideThread);
  }
  
  @Test
  public void testHideThread() {
    boolean hideThread = false;
    OEmbedRequest result = oEmbedRequest.HideThread(hideThread);
    
    assertEquals(hideThread, result.hideThread);
  }
  
  @Test
  public void testSetOmitScript() {
    boolean omitScript = true;
    oEmbedRequest.setOmitScript(omitScript);
    
    assertEquals(omitScript, oEmbedRequest.omitScript);
  }
  
  @Test
  public void testOmitScript() {
    boolean omitScript = true;
    OEmbedRequest result = oEmbedRequest.omitScript(omitScript);
    
    assertEquals(omitScript, result.omitScript);
  }
  
  @Test
  public void testSetAlign() {
    OEmbedRequest.Align align = OEmbedRequest.Align.LEFT;
    oEmbedRequest.setAlign(align);
    
    assertEquals(align, oEmbedRequest.align);
  }
  
  @Test
  public void testAlign() {
    OEmbedRequest.Align align = OEmbedRequest.Align.RIGHT;
    OEmbedRequest result = oEmbedRequest.align(align);
    
    assertEquals(align, result.align);
  }
  
  @Test
  public void testSetRelated() {
    String[] related = {"related1", "related2"};
    oEmbedRequest.setRelated(related);
    
    assertArrayEquals(related, oEmbedRequest.related);
  }
  
  @Test
  public void testRelated() {
    String[] related = {"related1", "related2"};
    OEmbedRequest result = oEmbedRequest.related(related);
    
    assertArrayEquals(related, result.related);
  }
  
  @Test
  public void testSetLang() {
    String lang = "en";
    oEmbedRequest.setLang(lang);
    
    assertEquals(lang, oEmbedRequest.lang);
  }
  
  @Test
  public void testLang() {
    String lang = "fr";
    OEmbedRequest result = oEmbedRequest.lang(lang);
    
    assertEquals(lang, result.lang);
  }
  
  @Test
  public void testAsHttpParameterArray() {
    HttpParameter[] expected = {new HttpParameter("id", String.valueOf(statusId)), new HttpParameter("url", url), new HttpParameter("maxwidth", oEmbedRequest.maxWidth), new HttpParameter("hide_media", oEmbedRequest.hideMedia), new HttpParameter("hide_thread", oEmbedRequest.hideThread), new HttpParameter("omit_script", oEmbedRequest.omitScript), new HttpParameter("align", oEmbedRequest.align.name().toLowerCase()), new HttpParameter("lang", oEmbedRequest.lang)};
    
    HttpParameter[] result = oEmbedRequest.asHttpParameterArray();
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testEquals() {
    OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
    
    assertTrue(oEmbedRequest.equals(oEmbedRequest2));
  }
  
  @Test
  public void testHashCode() {
    OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
    
    assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
  }
  
  @Test
  public void testToString() {
    String expected = "OEmbedRequest{statusId=" + statusId + ", url='" + url + '\'' + ", maxWidth=" + oEmbedRequest.maxWidth + ", hideMedia=" + oEmbedRequest.hideMedia + ", hideThread=" + oEmbedRequest.hideThread + ", omitScript=" + oEmbedRequest.omitScript + ", align=" + oEmbedRequest.align + ", related=" + ((oEmbedRequest.related == null) ? null : Arrays.asList(oEmbedRequest.related)) + ", lang='" + oEmbedRequest.lang + '\'' + '}';
    
    String result = oEmbedRequest.toString();
    
    assertEquals(expected, result);
  }
}