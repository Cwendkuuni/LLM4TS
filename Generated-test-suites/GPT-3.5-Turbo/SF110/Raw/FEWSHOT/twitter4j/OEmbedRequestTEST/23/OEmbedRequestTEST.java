package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        assertEquals(123, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertEquals(0, oEmbedRequest.maxWidth);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertArrayEquals(new String[0], oEmbedRequest.related);
        assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        HttpParameter[] expected = {
                new HttpParameter("id", "123"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", 500),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "none"),
                new HttpParameter("related", "related1,related2"),
                new HttpParameter("lang", "en")
        };
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        String[] related1 = {"related1", "related2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("en");

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        String[] related2 = {"related1", "related2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("en");

        assertEquals(oEmbedRequest1, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        String[] related1 = {"related1", "related2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("en");

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        String[] related2 = {"related1", "related2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("en");

        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        String expected = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=500, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[related1, related2], lang='en'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}