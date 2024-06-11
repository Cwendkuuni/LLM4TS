import static org.junit.Assert.*;
import org.junit.*;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, oEmbedRequest.related);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expected = {new HttpParameter("id", "12345"),
                                    new HttpParameter("url", "https://example.com"),
                                    new HttpParameter("maxwidth", 500),
                                    new HttpParameter("hide_media", false),
                                    new HttpParameter("hide_thread", false),
                                    new HttpParameter("omit_script", true),
                                    new HttpParameter("align", "center"),
                                    new HttpParameter("related", "related1 related2"),
                                    new HttpParameter("lang", "en")};
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest1.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest1.setLang("en");
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest2.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest2.setLang("en");
        
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest1.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest1.setLang("en");
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest2.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest2.setLang("en");
        
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"related1", "related2"});
        oEmbedRequest.setLang("en");
        
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[related1, related2], lang='en'}";
        
        assertEquals(expected, oEmbedRequest.toString());
    }
}