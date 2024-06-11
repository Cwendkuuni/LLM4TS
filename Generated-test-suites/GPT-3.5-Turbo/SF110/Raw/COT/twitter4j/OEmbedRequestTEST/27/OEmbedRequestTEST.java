
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
        OEmbedRequest result = request.MaxWidth(500);
        assertEquals(500, result.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.HideMedia(true);
        assertTrue(result.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.HideThread(true);
        assertTrue(result.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(false);
        assertFalse(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.omitScript(false);
        assertFalse(result.omitScript);
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
        OEmbedRequest result = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
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
        OEmbedRequest result = request.related(related);
        assertArrayEquals(related, result.related);
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
        OEmbedRequest result = request.lang("en");
        assertEquals("en", result.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(12, params.length);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testNotEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(54321, "https://example.com");
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertNotNull(request.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertNotNull(request.toString());
    }
}

