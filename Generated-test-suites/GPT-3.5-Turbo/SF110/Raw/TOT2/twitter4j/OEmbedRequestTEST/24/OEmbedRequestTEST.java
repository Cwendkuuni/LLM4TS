import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    // Method 1: public OEmbedRequest(final long statusId, final String url)
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(12345L, request.statusId);
        assertEquals("https://example.com", request.url);
    }

    // Method 2: public void setMaxWidth(final int maxWidth)
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setMaxWidth(200);
        assertEquals(200, request.maxWidth);
    }

    // Method 3: public OEmbedRequest MaxWidth(final int maxWidth)
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest updatedRequest = request.MaxWidth(300);
        assertEquals(300, updatedRequest.maxWidth);
    }

    // Method 4: public void setHideMedia(final boolean hideMedia)
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    // Method 5: public OEmbedRequest HideMedia(final boolean hideMedia)
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest updatedRequest = request.HideMedia(true);
        assertTrue(updatedRequest.hideMedia);
    }

    // Method 6: public void setHideThread(final boolean hideThread)
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    // Method 7: public OEmbedRequest HideThread(final boolean hideThread)
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest updatedRequest = request.HideThread(true);
        assertTrue(updatedRequest.hideThread);
    }

    // Method 8: public void setOmitScript(final boolean omitScript)
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    // Method 9: public OEmbedRequest omitScript(final boolean omitScript)
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest updatedRequest = request.omitScript(false);
        assertFalse(updatedRequest.omitScript);
    }

    // Method 10: public void setAlign(final Align align)
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    // Method 11: public OEmbedRequest align(final Align align)
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, updatedRequest.align);
    }

    // Method 12: public void setRelated(final String[] related)
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"tweet1", "tweet2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    // Method 13: public OEmbedRequest related(final String[] related)
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"tweet3", "tweet4"};
        OEmbedRequest updatedRequest = request.related(related);
        assertArrayEquals(related, updatedRequest.related);
    }

    // Additional edge cases and exception handling scenarios can be added as needed for comprehensive testing

}