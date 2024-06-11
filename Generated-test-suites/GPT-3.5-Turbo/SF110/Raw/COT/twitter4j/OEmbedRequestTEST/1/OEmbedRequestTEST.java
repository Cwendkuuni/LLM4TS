
import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(123456789, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertEquals(true, oEmbedRequest.hideMedia);
        assertEquals(true, oEmbedRequest.hideThread);
        assertEquals(false, oEmbedRequest.omitScript);
        assertEquals(Align.NONE, oEmbedRequest.align);
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
        assertEquals(500, result.maxWidth);
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
        assertEquals(false, result.hideMedia);
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
        assertEquals(false, result.hideThread);
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
        assertEquals(true, result.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setAlign(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(Align.CENTER);
        assertEquals(Align.CENTER, result.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.related(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, result.related);
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
        assertEquals("en", result.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expected = {
            new HttpParameter("id", "123456789"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 500),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com");
        
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testNotEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(987654321, "https://example.com");
        
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        int expected = oEmbedRequest.statusId.hashCode() + oEmbedRequest.url.hashCode() + oEmbedRequest.maxWidth + 
            (oEmbedRequest.hideMedia ? 1 : 0) + (oEmbedRequest.hideThread ? 1 : 0) + (oEmbedRequest.omitScript ? 1 : 0) + 
            (oEmbedRequest.align != null ? oEmbedRequest.align.hashCode() : 0) + Arrays.hashCode(oEmbedRequest.related) + 
            (oEmbedRequest.lang != null ? oEmbedRequest.lang.hashCode() : 0);
        
        assertEquals(expected, oEmbedRequest.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        
        assertEquals(expected, oEmbedRequest.toString());
    }
}
