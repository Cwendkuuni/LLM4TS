package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OEmbedRequestTest {
    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(12345L, request.statusId);
        assertEquals("https://example.com", request.url);
        assertNull(request.lang);
        assertEquals(0, request.maxWidth);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setMaxWidth(200);
        assertEquals(200, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = request.MaxWidth(200);
        assertEquals(200, result.maxWidth);
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
        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = request.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, result.align);
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
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "12345"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 200),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "right"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        assertTrue(request1.equals(request2));
        
        OEmbedRequest request3 = new OEmbedRequest(54321L, "https://example.com");
        assertFalse(request1.equals(request3));
        
        OEmbedRequest request4 = new OEmbedRequest(12345L, "https://anotherurl.com");
        assertFalse(request1.equals(request4));
        
        OEmbedRequest request5 = new OEmbedRequest(12345L, "https://example.com");
        request5.setMaxWidth(200);
        assertFalse(request1.equals(request5));
        
        OEmbedRequest request6 = new OEmbedRequest(12345L, "https://example.com");
        request6.setHideMedia(false);
        assertFalse(request1.equals(request6));
        
        OEmbedRequest request7 = new OEmbedRequest(12345L, "https://example.com");
        request7.setAlign(OEmbedRequest.Align.RIGHT);
        assertFalse(request1.equals(request7));
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
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        String expectedString = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=right, related=[user1, user2], lang='en'}";
        assertEquals(expectedString, request.toString());
    }
}