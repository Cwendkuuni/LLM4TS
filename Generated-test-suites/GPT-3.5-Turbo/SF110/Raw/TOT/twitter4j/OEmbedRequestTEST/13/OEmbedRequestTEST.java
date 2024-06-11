import static org.junit.Assert.*;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        assertNotNull(request);
        assertEquals(12345, request.statusId);
        assertEquals("https://www.example.com", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = new HttpParameter[9];
        expectedParams[0] = new HttpParameter("id", "12345");
        expectedParams[1] = new HttpParameter("url", "https://www.example.com");
        expectedParams[2] = new HttpParameter("maxwidth", 500);
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", "center");
        expectedParams[7] = new HttpParameter("related", "user1,user2");
        expectedParams[8] = new HttpParameter("lang", "en");
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://www.example.com");
        OEmbedRequest request3 = new OEmbedRequest(54321, "https://www.example.com");
        
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://www.example.com");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://www.example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        String expectedToString = "OEmbedRequest{statusId=12345, url='https://www.example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        
        assertEquals(expectedToString, request.toString());
    }

}