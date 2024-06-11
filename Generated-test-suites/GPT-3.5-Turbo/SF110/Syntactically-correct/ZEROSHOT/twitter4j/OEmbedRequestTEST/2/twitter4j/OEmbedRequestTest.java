import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setMaxWidth(400);
        assertEquals(400, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, result.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertFalse(result.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertFalse(result.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertTrue(result.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, result.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("fr");
        assertEquals("fr", result.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        int expectedSize = 12;
        HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
        assertEquals(expectedSize, params.length);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456, "https://example.com");
        assertEquals(oEmbedRequest1, oEmbedRequest2);
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        int expectedHashCode = -416107708;
        assertEquals(expectedHashCode, oEmbedRequest.hashCode());
    }
    
    @Test
    public void testToString() {
        long statusId = 123456;
        String url = "https://example.com";
        int maxWidth = 400;
        boolean hideMedia = true;
        boolean hideThread = true;
        boolean omitScript = false;
        OEmbedRequest.Align align = OEmbedRequest.Align.NONE;
        String[] related = {};
        String lang = null;
        
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(maxWidth);
        oEmbedRequest.setHideMedia(hideMedia);
        oEmbedRequest.setHideThread(hideThread);
        oEmbedRequest.setOmitScript(omitScript);
        oEmbedRequest.setAlign(align);
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang(lang);
        
        String expectedToString = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=400, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        
        assertEquals(expectedToString, oEmbedRequest.toString());
    }
}