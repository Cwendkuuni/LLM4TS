import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequest() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        
        assertEquals(123456789, request.statusId);
        assertEquals("https://www.example.com", request.url);
        assertEquals(0, request.maxWidth);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
        assertNull(request.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setMaxWidth(500);
        
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest updatedRequest = request.MaxWidth(800);
        
        assertEquals(800, request.maxWidth);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setHideMedia(false);
        
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest updatedRequest = request.HideMedia(false);
        
        assertFalse(request.hideMedia);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setHideThread(false);
        
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest updatedRequest = request.HideThread(false);
        
        assertFalse(request.hideThread);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setOmitScript(true);
        
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest updatedRequest = request.omitScript(true);
        
        assertTrue(request.omitScript);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.RIGHT);
        
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        
        assertEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest updatedRequest = request.related(related);
        
        assertEquals(related, request.related);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setLang("en");
        
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest updatedRequest = request.lang("fr");
        
        assertEquals("fr", request.lang);
        assertEquals(request, updatedRequest);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        String[] related = {"user1", "user2"};
        request.setMaxWidth(800);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expected = new HttpParameter[] {
            new HttpParameter("id", "123456789"),
            new HttpParameter("url", "https://www.example.com"),
            new HttpParameter("maxwidth", "800"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expected, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest request3 = new OEmbedRequest(987654321, "https://www.example.com");
        OEmbedRequest request4 = new OEmbedRequest(123456789, "https://www.twitter.com");
        OEmbedRequest request5 = new OEmbedRequest(123456789, "https://www.example.com");
        request5.setMaxWidth(800);
        
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
        assertFalse(request1.equals(request4));
        assertFalse(request1.equals(request5));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://www.example.com");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://www.example.com");
        request.setMaxWidth(800);
        String[] related = {"user1", "user2"};
        request.setHideMedia(false);
        request.setHideThread(false);
        request.omitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(related);
        request.setLang("en");
        
        String expected = "OEmbedRequest{statusId=123456789, url='https://www.example.com', maxWidth=800, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        
        assertEquals(expected, request.toString());
    }
}