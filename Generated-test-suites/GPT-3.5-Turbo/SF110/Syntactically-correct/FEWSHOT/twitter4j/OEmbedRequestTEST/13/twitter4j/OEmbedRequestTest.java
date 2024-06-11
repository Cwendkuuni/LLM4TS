package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testOEmbedRequest() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        assertNotNull(oEmbedRequest);
        
        assertEquals(123456789, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertEquals(true, oEmbedRequest.hideMedia);
        assertEquals(true, oEmbedRequest.hideThread);
        assertEquals(false, oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertArrayEquals(new String[0], oEmbedRequest.related);
        assertNull(oEmbedRequest.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.related);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.lang("en");
        assertEquals("en", oEmbedRequest.lang);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        HttpParameter[] expected = {
            new HttpParameter("id", "123456789"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 0),
            new HttpParameter("hide_media", true),
            new HttpParameter("hide_thread", true),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "none"),
            new HttpParameter("lang", null)
        };
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(987654321, "https://example.com");
        
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com");
        
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}