import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        assertEquals(123456, request.statusId);
        assertEquals("https://example.com", request.url);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertNotNull(params);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        assertEquals(request1, request2);
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        int hashCode = request.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String result = request.toString();
        assertNotNull(result);
    }
}