package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OEmbedRequestTest {

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
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setMaxWidth(500);
        
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setHideMedia(false);
        
        assertEquals(false, request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setHideThread(false);
        
        assertEquals(false, request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setOmitScript(true);
        
        assertEquals(true, request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.LEFT);
        
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setRelated(new String[]{"related1", "related2"});
        
        assertEquals(2, request.related.length);
        assertEquals("related1", request.related[0]);
        assertEquals("related2", request.related[1]);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setLang("en");
        
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");
        
        HttpParameter[] parameters = request.asHttpParameterArray();
        
        assertEquals(10, parameters.length);
        
        assertEquals("id", parameters[0].getName());
        assertEquals("123456", parameters[0].getValue());
        
        assertEquals("url", parameters[1].getName());
        assertEquals("https://example.com", parameters[1].getValue());
        
        assertEquals("maxwidth", parameters[2].getName());
        assertEquals("500", parameters[2].getValue());
        
        assertEquals("hide_media", parameters[3].getName());
        assertEquals("false", parameters[3].getValue());
        
        assertEquals("hide_thread", parameters[4].getName());
        assertEquals("false", parameters[4].getValue());
        
        assertEquals("omit_script", parameters[5].getName());
        assertEquals("true", parameters[5].getValue());
        
        assertEquals("align", parameters[6].getName());
        assertEquals("left", parameters[6].getValue());
        
        assertEquals("related", parameters[7].getName());
        assertEquals("related1,related2", parameters[7].getValue());
        
        assertEquals("lang", parameters[8].getName());
        assertEquals("en", parameters[8].getValue());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");
        
        int hashCode = request.hashCode();
        
        assertEquals(200800465, hashCode);
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");
        
        String toString = request.toString();
        
        assertEquals("OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}", toString);
    }
}