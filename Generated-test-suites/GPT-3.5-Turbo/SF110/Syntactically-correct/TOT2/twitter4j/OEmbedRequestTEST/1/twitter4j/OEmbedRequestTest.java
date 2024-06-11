package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.HideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.omitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.lang("en-us");
        assertEquals("en-us", oEmbedRequest.lang);
    }

    // Add test cases for edge cases and exceptions here

}