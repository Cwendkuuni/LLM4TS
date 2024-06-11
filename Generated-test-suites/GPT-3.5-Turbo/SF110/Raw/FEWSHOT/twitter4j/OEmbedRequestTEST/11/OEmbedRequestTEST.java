import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        assertEquals(123456, oEmbedRequest.getStatusId());
        assertEquals("https://www.example.com", oEmbedRequest.getUrl());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.getMaxWidth());
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest modified = oEmbedRequest.maxWidth(500);
        assertEquals(500, modified.getMaxWidth());
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest modified = oEmbedRequest.HideMedia(false);
        assertFalse(modified.isHideMedia());
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest modified = oEmbedRequest.HideThread(false);
        assertFalse(modified.isHideThread());
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest modified = oEmbedRequest.omitScript(true);
        assertTrue(modified.isOmitScript());
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.getAlign());
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest modified = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, modified.getAlign());
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        String[] related = {"related1", "related2"};
        OEmbedRequest modified = oEmbedRequest.related(related);
        assertArrayEquals(related, modified.getRelated());
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest modified = oEmbedRequest.lang("en");
        assertEquals("en", modified.getLang());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.HideMedia(false);
        oEmbedRequest.HideThread(false);
        oEmbedRequest.omitScript(true);
        oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oEmbedRequest.related(related);
        oEmbedRequest.lang("en");
        
        HttpParameter[] expectedParams = new HttpParameter[12];
        expectedParams[0] = new HttpParameter("id", "123456");
        expectedParams[1] = new HttpParameter("url", "https://www.example.com");
        expectedParams[2] = new HttpParameter("maxwidth", "500");
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", "center");
        expectedParams[7] = new HttpParameter("related", "related1,related2");
        expectedParams[8] = new HttpParameter("lang", "en");
        
        HttpParameter[] actualParams = oEmbedRequest.asHttpParameterArray();
        
        assertArrayEquals(expectedParams, actualParams);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456, "https://www.example.com");
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456, "https://www.example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456, "https://www.example.com");
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://www.example.com");
        assertEquals("OEmbedRequest{statusId=123456, url='https://www.example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", oEmbedRequest.toString());
    }
}