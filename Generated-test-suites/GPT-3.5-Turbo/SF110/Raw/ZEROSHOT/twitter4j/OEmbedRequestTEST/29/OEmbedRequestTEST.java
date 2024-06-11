import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setMaxWidth(800);
        assertEquals(800, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setMaxWidth(800);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = new HttpParameter[12];
        expectedParams[0] = new HttpParameter("id", "1234");
        expectedParams[1] = new HttpParameter("url", "https://example.com");
        expectedParams[2] = new HttpParameter("maxwidth", "800");
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", "right");
        expectedParams[7] = new HttpParameter("related", "related1,related2");
        expectedParams[8] = new HttpParameter("lang", "en");
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1234, "https://example.com");
        request1.setMaxWidth(800);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        request1.setRelated(related);
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(1234, "https://example.com");
        request2.setMaxWidth(800);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        request2.setRelated(related);
        request2.setLang("en");
        
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1234, "https://example.com");
        request1.setMaxWidth(800);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        request1.setRelated(related);
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(1234, "https://example.com");
        request2.setMaxWidth(800);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        request2.setRelated(related);
        request2.setLang("en");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setMaxWidth(800);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        
        String expectedOutput = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=800, hideMedia=false, hideThread=false, omitScript=true, align=RIGHT, related=[related1, related2], lang='en'}";
        assertEquals(expectedOutput, request.toString());
    }
}