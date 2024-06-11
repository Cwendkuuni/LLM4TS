import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        assertEquals(1234567890, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertArrayEquals(new String[0], oEmbedRequest.related);
        assertNull(oEmbedRequest.lang);
    }
    
    // Test setMaxWidth method
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    // Test setHideMedia method
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }
    
    // Test setHideThread method
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }
    
    // Test setOmitScript method
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }
    
    // Test setAlign method
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    // Test setRelated method
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.related);
    }
    
    // Test setLang method
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    // Test asHttpParameterArray method
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");
        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    // Test equals method
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890, "https://example.com");
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        
        oEmbedRequest1.setMaxWidth(500);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    // Test hashCode method
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234567890, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890, "https://example.com");
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
        
        oEmbedRequest1.setMaxWidth(500);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890, "https://example.com");
        String expected = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}