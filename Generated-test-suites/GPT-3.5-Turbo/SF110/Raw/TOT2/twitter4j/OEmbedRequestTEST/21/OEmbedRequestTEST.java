import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");

        assertEquals(12345L, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertEquals(0, oEmbedRequest.related.length);
        assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);

        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setHideMedia(false);

        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setHideThread(false);

        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setOmitScript(true);

        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);

        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});

        assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setLang("en");

        assertEquals("en", oEmbedRequest.lang);
    }

    // Add more test cases for edge cases and exceptions

}