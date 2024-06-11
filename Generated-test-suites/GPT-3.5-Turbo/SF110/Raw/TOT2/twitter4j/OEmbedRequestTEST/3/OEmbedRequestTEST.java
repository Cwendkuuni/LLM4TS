import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setRelated(new String[]{"twitter", "api"});
        assertArrayEquals(new String[]{"twitter", "api"}, request.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    // Add more specific test cases for edge cases and exception handling scenarios
    
}