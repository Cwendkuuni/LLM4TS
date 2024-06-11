import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setOmitScript(false);
        assertFalse(request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    // Test case for typical input for related method
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    // Test case for null input for related method
    @Test
    public void testRelatedNullInput() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = null;
        request.setRelated(related);
        assertArrayEquals(new String[0], request.related);
    }
    
    // Test case for typical input for lang method
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    // Test case for null input for lang method
    @Test
    public void testLangNullInput() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setLang(null);
        assertNull(request.lang);
    }
}