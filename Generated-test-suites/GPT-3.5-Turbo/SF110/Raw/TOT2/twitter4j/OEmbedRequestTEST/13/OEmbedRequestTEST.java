import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        
        assertEquals(1234L, request.statusId);
        assertEquals("https://example.com", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        request.setMaxWidth(500);
        
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        request.setHideMedia(false);
        
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        request.setHideThread(false);
        
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        request.setOmitScript(true);
        
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1234L, "https://example.com");
        request.setLang("en");
        
        assertEquals("en", request.lang);
    }

}