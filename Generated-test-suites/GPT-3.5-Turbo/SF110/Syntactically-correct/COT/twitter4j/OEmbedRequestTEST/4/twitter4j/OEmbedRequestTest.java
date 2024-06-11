
import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(200);
        assertEquals(200, oEmbedRequest.maxWidth);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertFalse(oEmbedRequest.hideThread);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(false);
        assertFalse(oEmbedRequest.omitScript);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, oEmbedRequest.align);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"abc", "def"});
        assertArrayEquals(new String[]{"abc", "def"}, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.related(new String[]{"xyz"});
        assertArrayEquals(new String[]{"xyz"}, oEmbedRequest.related);
        assertSame(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.lang);
        assertSame(oEmbedRequest, result);
    }
}
