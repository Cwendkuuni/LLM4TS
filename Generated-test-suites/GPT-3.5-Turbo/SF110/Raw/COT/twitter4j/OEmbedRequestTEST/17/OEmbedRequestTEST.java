import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    // Test Constructor
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        
        assertEquals(123456789, oEmbedRequest.getStatusId());
        assertEquals("https://example.com", oEmbedRequest.getUrl());
        assertEquals(true, oEmbedRequest.getHideMedia());
        assertEquals(true, oEmbedRequest.getHideThread());
        assertEquals(false, oEmbedRequest.getOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.getAlign());
        assertEquals(0, oEmbedRequest.getRelated().length);
        assertNull(oEmbedRequest.getLang());
    }
    
    // Test setMaxWidth
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        
        assertEquals(500, oEmbedRequest.getMaxWidth());
    }
    
    // Test MaxWidth
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        
        assertEquals(500, result.getMaxWidth());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test setHideMedia
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        
        assertEquals(false, oEmbedRequest.getHideMedia());
    }
    
    // Test HideMedia
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        
        assertEquals(false, result.getHideMedia());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test setHideThread
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setHideThread(false);
        
        assertEquals(false, oEmbedRequest.getHideThread());
    }
    
    // Test HideThread
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        
        assertEquals(false, result.getHideThread());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test setOmitScript
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        
        assertEquals(true, oEmbedRequest.getOmitScript());
    }
    
    // Test omitScript
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        
        assertEquals(true, result.getOmitScript());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test setAlign
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.getAlign());
    }
    
    // Test align
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        
        assertEquals(OEmbedRequest.Align.LEFT, result.getAlign());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test setRelated
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        
        assertArrayEquals(related, oEmbedRequest.getRelated());
    }
    
    // Test related
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        
        assertArrayEquals(related, result.getRelated());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test setLang
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setLang("en");
        
        assertEquals("en", oEmbedRequest.getLang());
    }
    
    // Test lang
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("en");
        
        assertEquals("en", result.getLang());
        assertEquals(oEmbedRequest, result);
    }
    
    // Test asHttpParameterArray
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] result = oEmbedRequest.asHttpParameterArray();
        
        assertEquals(12, result.length);
        assertEquals(new HttpParameter("id", "123456789"), result[0]);
        assertEquals(new HttpParameter("url", "https://example.com"), result[1]);
        assertEquals(new HttpParameter("maxwidth", 500), result[2]);
        assertEquals(new HttpParameter("hide_media", false), result[3]);
        assertEquals(new HttpParameter("hide_thread", false), result[4]);
        assertEquals(new HttpParameter("omit_script", true), result[5]);
        assertEquals(new HttpParameter("align", "left"), result[6]);
        assertEquals(new HttpParameter("related", "user1,user2"), result[7]);
        assertEquals(new HttpParameter("lang", "en"), result[8]);
        
    }
    
    // Test equals
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest3.setMaxWidth(500);
        
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
        assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
    }
    
    // Test hashCode
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest oEmbedRequest3 = new OEmbedRequest(123456789, "https://example.com");
        
        oEmbedRequest3.setMaxWidth(500);
        
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest3.hashCode());
    }
    
    // Test toString
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        String result = oEmbedRequest.toString();
        
        String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[user1, user2], lang='en'}";
        
        assertEquals(expected, result);
    }

}