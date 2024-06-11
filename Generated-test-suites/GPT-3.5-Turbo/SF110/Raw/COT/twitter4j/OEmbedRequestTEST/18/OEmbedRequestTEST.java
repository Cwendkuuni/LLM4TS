import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest result = oEmbedRequest.maxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest result = oEmbedRequest.hideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest result = oEmbedRequest.hideThread(false);
        assertFalse(oEmbedRequest.hideThread);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertTrue(oEmbedRequest.omitScript);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        String[] related = {"tweet1", "tweet2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.related);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("en");
        assertEquals("en", oEmbedRequest.lang);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        HttpParameter[] expected = {
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 500),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "tweet1,tweet2"),
            new HttpParameter("lang", "en")
        };
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.CENTER);
        String[] related1 = {"tweet1", "tweet2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("en");

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        String[] related2 = {"tweet1", "tweet2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("en");

        assertEquals(oEmbedRequest1, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.CENTER);
        String[] related1 = {"tweet1", "tweet2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("en");

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        String[] related2 = {"tweet1", "tweet2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("en");

        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        String expected = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[tweet1, tweet2], lang='en'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}