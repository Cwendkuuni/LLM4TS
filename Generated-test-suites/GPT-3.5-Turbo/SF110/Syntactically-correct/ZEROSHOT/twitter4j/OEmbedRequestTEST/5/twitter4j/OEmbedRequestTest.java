import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(1234567890L, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertArrayEquals(new String[0], oEmbedRequest.related);
        assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest maxWidthOEmbedRequest = oEmbedRequest.MaxWidth(500);
        assertEquals(500, maxWidthOEmbedRequest.maxWidth);
        assertSame(oEmbedRequest, maxWidthOEmbedRequest);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest hideMediaOEmbedRequest = oEmbedRequest.HideMedia(false);
        assertFalse(hideMediaOEmbedRequest.hideMedia);
        assertSame(oEmbedRequest, hideMediaOEmbedRequest);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest hideThreadOEmbedRequest = oEmbedRequest.HideThread(false);
        assertFalse(hideThreadOEmbedRequest.hideThread);
        assertSame(oEmbedRequest, hideThreadOEmbedRequest);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest omitScriptOEmbedRequest = oEmbedRequest.omitScript(true);
        assertTrue(omitScriptOEmbedRequest.omitScript);
        assertSame(oEmbedRequest, omitScriptOEmbedRequest);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest alignOEmbedRequest = oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, alignOEmbedRequest.align);
        assertSame(oEmbedRequest, alignOEmbedRequest);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest relatedOEmbedRequest = oEmbedRequest.related(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, relatedOEmbedRequest.related);
        assertSame(oEmbedRequest, relatedOEmbedRequest);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest langOEmbedRequest = oEmbedRequest.lang("es");
        assertEquals("es", langOEmbedRequest.lang);
        assertSame(oEmbedRequest, langOEmbedRequest);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");

        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "left"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };

        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.RIGHT);
        oEmbedRequest1.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest1.setLang("es");

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.RIGHT);
        oEmbedRequest2.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest2.setLang("es");

        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));

        oEmbedRequest2.setHideMedia(true);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.RIGHT);
        oEmbedRequest1.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest1.setLang("es");

        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.RIGHT);
        oEmbedRequest2.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest2.setLang("es");

        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());

        oEmbedRequest2.setHideMedia(true);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("es");

        String expected = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=0, hideMedia=false, hideThread=false, omitScript=false, align=RIGHT, related=[related1, related2], lang='es'}";

        assertEquals(expected, oEmbedRequest.toString());
    }

}