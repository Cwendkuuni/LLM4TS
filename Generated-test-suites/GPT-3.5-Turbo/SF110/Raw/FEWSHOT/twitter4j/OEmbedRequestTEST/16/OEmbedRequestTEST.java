package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequest() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");

        assertEquals(12345, oEmbedRequest.statusId);
        assertEquals("https://twitter.com/example/status/12345", oEmbedRequest.url);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertArrayEquals(new String[0], oEmbedRequest.related);
        assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setMaxWidth(500);

        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);

        assertEquals(500, oEmbedRequest.maxWidth);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setHideMedia(false);

        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);

        assertFalse(oEmbedRequest.hideMedia);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setHideThread(false);

        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest result = oEmbedRequest.HideThread(false);

        assertFalse(oEmbedRequest.hideThread);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setOmitScript(true);

        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest result = oEmbedRequest.omitScript(true);

        assertTrue(oEmbedRequest.omitScript);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);

        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);

        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);

        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        String[] related = {"user1", "user2"};
        OEmbedRequest result = oEmbedRequest.related(related);

        assertArrayEquals(related, oEmbedRequest.related);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setLang("en");

        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest result = oEmbedRequest.lang("en");

        assertEquals("en", oEmbedRequest.lang);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expected = {
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "https://twitter.com/example/status/12345"),
                new HttpParameter("maxwidth", "500"),
                new HttpParameter("hide_media", false),
                new HttpParameter("hide_thread", false),
                new HttpParameter("omit_script", true),
                new HttpParameter("align", "center"),
                new HttpParameter("related", "user1,user2"),
                new HttpParameter("lang", "en")
        };

        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        String[] related = {"user1", "user2"};
        oEmbedRequest1.setRelated(related);
        oEmbedRequest2.setRelated(related);

        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        String[] related = {"user1", "user2"};
        oEmbedRequest1.setRelated(related);
        oEmbedRequest2.setRelated(related);

        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        String expected = "OEmbedRequest{statusId=12345, url='https://twitter.com/example/status/12345', maxWidth=0, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";

        assertEquals(expected, oEmbedRequest.toString());
    }
}