import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.OEmbedRequest;

public class OEmbedRequestTest {
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        assertEquals(123456, request.statusId);
        assertEquals("https://example.com", request.url);
        assertEquals(true, request.hideMedia);
        assertEquals(true, request.hideThread);
        assertEquals(false, request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
        assertNull(request.lang);
    }
    
    // Test case for setMaxWidth
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    // Test case for MaxWidth
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest updatedRequest = request.MaxWidth(800);
        assertEquals(800, updatedRequest.maxWidth);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for setHideMedia
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setHideMedia(false);
        assertEquals(false, request.hideMedia);
    }
    
    // Test case for HideMedia
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest updatedRequest = request.HideMedia(false);
        assertEquals(false, updatedRequest.hideMedia);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for setHideThread
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setHideThread(false);
        assertEquals(false, request.hideThread);
    }
    
    // Test case for HideThread
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest updatedRequest = request.HideThread(false);
        assertEquals(false, updatedRequest.hideThread);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for setOmitScript
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setOmitScript(true);
        assertEquals(true, request.omitScript);
    }
    
    // Test case for omitScript
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest updatedRequest = request.omitScript(true);
        assertEquals(true, updatedRequest.omitScript);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for setAlign
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    // Test case for align
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, updatedRequest.align);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for setRelated
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    // Test case for related
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        String[] related = {"user1", "user2"};
        OEmbedRequest updatedRequest = request.related(related);
        assertArrayEquals(related, updatedRequest.related);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for setLang
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    // Test case for lang
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest updatedRequest = request.lang("fr");
        assertEquals("fr", updatedRequest.lang);
        assertEquals(request, updatedRequest);
    }
    
    // Test case for asHttpParameterArray
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "123456"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "left"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    // Test case for equals
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        request1.setHideMedia(false);
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        request2.setHideMedia(false);
        request2.setLang("en");
        
        OEmbedRequest request3 = new OEmbedRequest(789, "https://example.com");
        request3.setHideMedia(true);
        request3.setLang("en");
        
        assertTrue(request1.equals(request2)); // Equal objects
        assertFalse(request1.equals(request3)); // Different statusId
    }
    
    // Test case for hashCode
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        request1.setHideMedia(false);
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        request2.setHideMedia(false);
        request2.setLang("en");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    // Test case for toString
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");
        
        String expectedString = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=left, related=[user1, user2], lang='en'}";
        
        assertEquals(expectedString, request.toString());
    }
}