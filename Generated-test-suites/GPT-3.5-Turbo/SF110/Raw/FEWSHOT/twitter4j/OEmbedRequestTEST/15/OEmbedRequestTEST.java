package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class OEmbedRequestTest {
    
    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        
        assertTrue(oEmbedRequest.getHideMedia());
        assertTrue(oEmbedRequest.getHideThread());
        assertFalse(oEmbedRequest.getOmitScript());
        assertEquals(Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(200);
        
        assertEquals(200, oEmbedRequest.getMaxWidth());
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(200);
        
        assertEquals(200, result.getMaxWidth());
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        
        assertFalse(oEmbedRequest.getHideMedia());
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        
        assertFalse(result.getHideMedia());
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setHideThread(false);
        
        assertFalse(oEmbedRequest.getHideThread());
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        
        assertFalse(result.getHideThread());
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        
        assertTrue(oEmbedRequest.getOmitScript());
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        
        assertTrue(result.getOmitScript());
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setAlign(Align.CENTER);
        
        assertEquals(Align.CENTER, oEmbedRequest.getAlign());
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(Align.RIGHT);
        
        assertEquals(Align.RIGHT, result.getAlign());
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        
        assertArrayEquals(related, oEmbedRequest.getRelated());
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        
        assertArrayEquals(related, result.getRelated());
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setLang("en");
        
        assertEquals("en", oEmbedRequest.getLang());
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("fr");
        
        assertEquals("fr", result.getLang());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expected = new HttpParameter[9];
        expected[0] = new HttpParameter("id", "123");
        expected[1] = new HttpParameter("url", "https://example.com");
        expected[2] = new HttpParameter("maxwidth", "200");
        expected[3] = new HttpParameter("hide_media", false);
        expected[4] = new HttpParameter("hide_thread", false);
        expected[5] = new HttpParameter("omit_script", true);
        expected[6] = new HttpParameter("align", "center");
        expected[7] = new HttpParameter("related", "user1,user2");
        expected[8] = new HttpParameter("lang", "en");
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest1.setMaxWidth(200);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(Align.CENTER);
        String[] related1 = {"user1", "user2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("en");
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setMaxWidth(200);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(Align.CENTER);
        String[] related2 = {"user1", "user2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("en");
        
        assertEquals(oEmbedRequest1, oEmbedRequest2);
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest1.setMaxWidth(200);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(Align.CENTER);
        String[] related1 = {"user1", "user2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("en");
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setMaxWidth(200);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(Align.CENTER);
        String[] related2 = {"user1", "user2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("en");
        
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        String expected = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        
        assertEquals(expected, oEmbedRequest.toString());
    }
}