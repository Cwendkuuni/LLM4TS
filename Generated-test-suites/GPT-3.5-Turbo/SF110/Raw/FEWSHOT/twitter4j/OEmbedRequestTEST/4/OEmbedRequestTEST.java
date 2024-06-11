package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OEmbedRequestTest {
    
    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        assertEquals(12345L, request.statusId);
        assertEquals("https://example.com", request.url);
        assertEquals(0, request.maxWidth);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setMaxWidth(500);
        
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        OEmbedRequest result = request.MaxWidth(500);
        
        assertEquals(500, result.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setHideMedia(false);
        
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        OEmbedRequest result = request.HideMedia(false);
        
        assertFalse(result.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setHideThread(false);
        
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        OEmbedRequest result = request.HideThread(false);
        
        assertFalse(result.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setOmitScript(true);
        
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        OEmbedRequest result = request.omitScript(true);
        
        assertTrue(result.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        OEmbedRequest result = request.align(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        String[] related = {"user1", "user2"};
        
        request.setRelated(related);
        
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        String[] related = {"user1", "user2"};
        
        OEmbedRequest result = request.related(related);
        
        assertArrayEquals(related, result.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setLang("en");
        
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        OEmbedRequest result = request.lang("en");
        
        assertEquals("en", result.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[] {"user1", "user2"});
        request.setLang("en");
        
        HttpParameter[] expected = {
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", "500"),
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
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        
        assertEquals(expected, request.toString());
    }
}