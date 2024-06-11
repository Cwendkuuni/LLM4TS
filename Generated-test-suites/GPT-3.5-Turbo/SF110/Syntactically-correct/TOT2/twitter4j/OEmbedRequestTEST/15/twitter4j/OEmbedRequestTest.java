package twitter4j;
import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        assertNotNull(request);
        assertEquals(123456789, request.statusId);
        assertEquals("https://example.com", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
        assertNull(request.lang);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    // Additional test cases for other methods would go here

}