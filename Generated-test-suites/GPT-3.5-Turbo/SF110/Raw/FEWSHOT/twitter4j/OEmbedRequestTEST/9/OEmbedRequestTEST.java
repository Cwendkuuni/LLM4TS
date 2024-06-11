package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class OEmbedRequestTest {

  @Test
  public void testOEmbedRequest() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    assertEquals(123456789, oembed.statusId);
    assertEquals("https://example.com", oembed.url);
    assertTrue(oembed.hideMedia);
    assertTrue(oembed.hideThread);
    assertFalse(oembed.omitScript);
    assertEquals("NONE", oembed.align.name());
    assertEquals(0, oembed.related.length);
    assertNull(oembed.lang);
  }

  @Test
  public void testSetMaxWidth() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    oembed.setMaxWidth(500);

    assertEquals(500, oembed.maxWidth);
  }

  @Test
  public void testMaxWidth() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    OEmbedRequest result = oembed.MaxWidth(500);

    assertEquals(500, result.maxWidth);
  }

  @Test
  public void testSetHideMedia() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    oembed.setHideMedia(false);

    assertFalse(oembed.hideMedia);
  }

  @Test
  public void testHideMedia() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    OEmbedRequest result = oembed.HideMedia(false);

    assertFalse(result.hideMedia);
  }

  @Test
  public void testSetHideThread() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    oembed.setHideThread(false);

    assertFalse(oembed.hideThread);
  }

  @Test
  public void testHideThread() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    OEmbedRequest result = oembed.HideThread(false);

    assertFalse(result.hideThread);
  }

  @Test
  public void testSetOmitScript() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    oembed.setOmitScript(true);

    assertTrue(oembed.omitScript);
  }

  @Test
  public void testOmitScript() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    OEmbedRequest result = oembed.omitScript(true);

    assertTrue(result.omitScript);
  }

  @Test
  public void testSetAlign() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    oembed.setAlign(OEmbedRequest.Align.CENTER);

    assertEquals(OEmbedRequest.Align.CENTER, oembed.align);
  }

  @Test
  public void testAlign() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    OEmbedRequest result = oembed.align(OEmbedRequest.Align.CENTER);

    assertEquals(OEmbedRequest.Align.CENTER, result.align);
  }

  @Test
  public void testSetRelated() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    String[] related = {"user1", "user2"};
    oembed.setRelated(related);

    assertArrayEquals(related, oembed.related);
  }

  @Test
  public void testRelated() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    String[] related = {"user1", "user2"};
    OEmbedRequest result = oembed.related(related);

    assertArrayEquals(related, result.related);
  }

  @Test
  public void testSetLang() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    oembed.setLang("en");

    assertEquals("en", oembed.lang);
  }

  @Test
  public void testLang() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");

    OEmbedRequest result = oembed.lang("en");

    assertEquals("en", result.lang);
  }

  @Test
  public void testAsHttpParameterArray() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");
    oembed.setMaxWidth(500);
    oembed.setHideMedia(false);
    oembed.setHideThread(false);
    oembed.setOmitScript(true);
    oembed.setAlign(OEmbedRequest.Align.LEFT);
    String[] related = {"user1", "user2"};
    oembed.setRelated(related);
    oembed.setLang("en");

    HttpParameter[] params = oembed.asHttpParameterArray();

    assertEquals(10, params.length);
    assertEquals("id", params[0].getKey());
    assertEquals("123456789", params[0].getValue());
    assertEquals("url", params[1].getKey());
    assertEquals("https://example.com", params[1].getValue());
    assertEquals("maxwidth", params[2].getKey());
    assertEquals("500", params[2].getValue());
  }

  @Test
  public void testEquals() {
    OEmbedRequest oembed1 = new OEmbedRequest(123456789, "https://example.com");
    OEmbedRequest oembed2 = new OEmbedRequest(123456789, "https://example.com");
    OEmbedRequest oembed3 = new OEmbedRequest(987654321, "https://example.org");

    assertTrue(oembed1.equals(oembed1));
    assertTrue(oembed1.equals(oembed2));
    assertFalse(oembed1.equals(oembed3));
    assertFalse(oembed1.equals(null));
    assertFalse(oembed1.equals("123456789"));
  }

  @Test
  public void testHashCode() {
    OEmbedRequest oembed1 = new OEmbedRequest(123456789, "https://example.com");
    OEmbedRequest oembed2 = new OEmbedRequest(123456789, "https://example.com");
    OEmbedRequest oembed3 = new OEmbedRequest(987654321, "https://example.org");

    assertEquals(oembed1.hashCode(), oembed2.hashCode());
    assertNotEquals(oembed1.hashCode(), oembed3.hashCode());
  }

  @Test
  public void testToString() {
    OEmbedRequest oembed = new OEmbedRequest(123456789, "https://example.com");
    oembed.setMaxWidth(500);
    oembed.setHideMedia(false);
    oembed.setHideThread(false);
    oembed.setOmitScript(true);
    oembed.setAlign(OEmbedRequest.Align.LEFT);
    String[] related = {"user1", "user2"};
    oembed.setRelated(related);
    oembed.setLang("en");

    String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[user1, user2], lang='en'}";

    assertEquals(expected, oembed.toString());
  }

}