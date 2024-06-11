import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    // Include more test cases for other methods
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");
        
        HttpParameter[] params = request.asHttpParameterArray();
        
        assertNotNull(params);
        assertEquals(8, params.length);
        // Add more assertions based on expected results
    }
    
    // Include test cases for equals(), hashCode(), toString(), and other methods
    
}