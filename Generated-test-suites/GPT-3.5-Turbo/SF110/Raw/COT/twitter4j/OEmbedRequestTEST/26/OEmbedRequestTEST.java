
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.MaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.HideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.HideThread(true);
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.omitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        String[] related = {"user1", "user2"};
        request.related(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1L, "https://example.com");
        request.lang("en");
        assertEquals("en", request.lang);
    }
}
