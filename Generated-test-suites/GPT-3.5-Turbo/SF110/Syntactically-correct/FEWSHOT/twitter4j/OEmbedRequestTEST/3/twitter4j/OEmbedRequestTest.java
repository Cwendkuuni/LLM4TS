import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(123, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
        assertEquals(0, request.getMaxWidth());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setMaxWidth(500);
        
        assertEquals(500, request.getMaxWidth());
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        OEmbedRequest result = request.MaxWidth(500);
        
        assertEquals(500, result.getMaxWidth());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setHideMedia(false);
        
        assertFalse(request.isHideMedia());
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        OEmbedRequest result = request.HideMedia(false);
        
        assertFalse(result.isHideMedia());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setHideThread(false);
        
        assertFalse(request.isHideThread());
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        OEmbedRequest result = request.HideThread(false);
        
        assertFalse(result.isHideThread());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setOmitScript(true);
        
        assertTrue(request.isOmitScript());
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        OEmbedRequest result = request.omitScript(true);
        
        assertTrue(result.isOmitScript());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, request.getAlign());
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        OEmbedRequest result = request.align(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, result.getAlign());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        
        assertArrayEquals(related, request.getRelated());
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        String[] related = {"user1", "user2"};
        OEmbedRequest result = request.related(related);
        
        assertArrayEquals(related, result.getRelated());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setLang("en");
        
        assertEquals("en", request.getLang());
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        OEmbedRequest result = request.lang("en");
        
        assertEquals("en", result.getLang());
        assertEquals(request, result); // Check if the original object is not modified
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");
        
        HttpParameter[] expectedParams = new HttpParameter[]{
            new HttpParameter("id", "123"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 500),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        
        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        
        OEmbedRequest request3 = new OEmbedRequest(456, "https://example.com");
        request3.setMaxWidth(500);
        request3.setHideMedia(false);
        request3.setHideThread(false);
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        
        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        
        OEmbedRequest request3 = new OEmbedRequest(456, "https://example.com");
        request3.setMaxWidth(500);
        request3.setHideMedia(false);
        request3.setHideThread(false);
        
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");
        
        String expectedToString = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        
        assertEquals(expectedToString, request.toString());
    }
}