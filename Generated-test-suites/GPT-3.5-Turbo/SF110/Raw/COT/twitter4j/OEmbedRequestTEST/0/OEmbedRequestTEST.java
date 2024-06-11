
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest modifiedRequest = request.MaxWidth(500);
        assertEquals(500, modifiedRequest.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertEquals(false, request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest modifiedRequest = request.HideMedia(false);
        assertEquals(false, modifiedRequest.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertEquals(false, request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest modifiedRequest = request.HideThread(false);
        assertEquals(false, modifiedRequest.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertEquals(true, request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest modifiedRequest = request.omitScript(true);
        assertEquals(true, modifiedRequest.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest modifiedRequest = request.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, modifiedRequest.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest modifiedRequest = request.related(related);
        assertArrayEquals(related, modifiedRequest.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest modifiedRequest = request.lang("fr");
        assertEquals("fr", modifiedRequest.lang);
    }
}
