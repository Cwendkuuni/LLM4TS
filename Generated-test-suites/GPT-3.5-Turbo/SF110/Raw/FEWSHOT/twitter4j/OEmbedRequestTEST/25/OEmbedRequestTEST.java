import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(123, request.statusId);
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
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setMaxWidth(200);
        assertEquals(200, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.MaxWidth(200));
        assertEquals(200, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.HideMedia(false));
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.HideThread(false));
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.omitScript(true));
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.align(OEmbedRequest.Align.LEFT));
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setRelated(new String[] {"related1", "related2"});
        assertArrayEquals(new String[] {"related1", "related2"}, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.related(new String[] {"related1", "related2"}));
        assertArrayEquals(new String[] {"related1", "related2"}, request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request, request.lang("en"));
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[] {"related1", "related2"});
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "123"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "200"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "left"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest request3 = new OEmbedRequest(456, "https://example.com");
        
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[] {"related1", "related2"});
        request.setLang("en");
        
        String expectedToString = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=left, related=[related1, related2], lang='en'}";
        
        assertEquals(expectedToString, request.toString());
    }
}