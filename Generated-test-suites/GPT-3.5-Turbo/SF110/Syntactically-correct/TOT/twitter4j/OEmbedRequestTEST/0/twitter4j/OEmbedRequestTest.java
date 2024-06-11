package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        // Typical use case
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(12345, request.statusId);
        assertEquals("https://example.com", request.url);
        
        // Edge case
        request = new OEmbedRequest(Long.MAX_VALUE, "https://example.com");
        assertEquals(Long.MAX_VALUE, request.statusId);
        assertEquals("https://example.com", request.url);
        
        // Error scenario: Invalid URL
        request = new OEmbedRequest(12345, "invalidurl");
        assertEquals(12345, request.statusId);
        assertNull(request.url);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
        
        // Edge case
        request.setMaxWidth(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, request.maxWidth);
        
        // Error scenario: Negative width
        request.setMaxWidth(-100);
        assertEquals(-100, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
        
        // Edge case
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        request.setHideThread(true);
        assertTrue(request.hideThread);
        
        // Edge case
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        request.setOmitScript(true);
        assertTrue(request.omitScript);
        
        // Edge case
        request.setOmitScript(false);
        assertFalse(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
        
        // Edge case
        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
        
        // Error scenario: Invalid alignment
        request.setAlign(null);
        assertNull(request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
        
        // Edge case: Empty array
        String[] emptyRelated = {};
        request.setRelated(emptyRelated);
        assertArrayEquals(emptyRelated, request.related);
        
        // Error scenario: Null array
        request.setRelated(null);
        assertNull(request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        
        // Typical use case
        request.setLang("en");
        assertEquals("en", request.lang);
        
        // Edge case: Empty lang
        request.setLang("");
        assertEquals("", request.lang);
        
        // Error scenario: Null lang
        request.setLang(null);
        assertNull(request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "12345"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", true),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "right"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(true);
        request1.setHideThread(false);
        request1.setOmitScript(false);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related1 = {"user1", "user2"};
        request1.setRelated(related1);
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(true);
        request2.setHideThread(false);
        request2.setOmitScript(false);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related2 = {"user1", "user2"};
        request2.setRelated(related2);
        request2.setLang("en");
        
        assertTrue(request1.equals(request2));
        
        // Changing one attribute should make the requests unequal
        request2.setLang("es");
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(true);
        request1.setHideThread(false);
        request1.setOmitScript(false);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related1 = {"user1", "user2"};
        request1.setRelated(related1);
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(true);
        request2.setHideThread(false);
        request2.setOmitScript(false);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related2 = {"user1", "user2"};
        request2.setRelated(related2);
        request2.setLang("en");
        
        assertEquals(request1.hashCode(), request2.hashCode());
        
        // Changing one attribute should change the hash code
        request2.setLang("es");
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=500, hideMedia=true, hideThread=false, omitScript=false, align=RIGHT, related=[user1, user2], lang='en'}";
        
        assertEquals(expected, request.toString());
    }
}