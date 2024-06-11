import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setMaxWidth(800);
        
        assertEquals(800, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest result = request.MaxWidth(800);
        
        assertEquals(800, result.maxWidth);
        assertEquals(request, result);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setHideMedia(true);
        
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest result = request.HideMedia(true);
        
        assertTrue(result.hideMedia);
        assertEquals(request, result);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setHideThread(true);
        
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest result = request.HideThread(true);
        
        assertTrue(result.hideThread);
        assertEquals(request, result);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setOmitScript(true);
        
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest result = request.omitScript(true);
        
        assertTrue(result.omitScript);
        assertEquals(request, result);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.CENTER);
        
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        OEmbedRequest result = request.align(OEmbedRequest.Align.CENTER);

        assertEquals(OEmbedRequest.Align.CENTER, result.align);
        assertEquals(request, result);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"user1", "user2"};
        
        request.setRelated(related);
        
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"user1", "user2"};
        
        OEmbedRequest result = request.related(related);
        
        assertArrayEquals(related, result.related);
        assertEquals(request, result);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String lang = "en";
        
        request.setLang(lang);
        
        assertEquals(lang, request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String lang = "en";
        
        OEmbedRequest result = request.lang(lang);
        
        assertEquals(lang, result.lang);
        assertEquals(request, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(800);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expected = {
            new HttpParameter("id", String.valueOf(request.statusId)),
            new HttpParameter("url", request.url),
            new HttpParameter("maxwidth", String.valueOf(request.maxWidth)),
            new HttpParameter("hide_media", request.hideMedia),
            new HttpParameter("hide_thread", request.hideThread),
            new HttpParameter("omit_script", request.omitScript),
            new HttpParameter("align", request.align.name().toLowerCase()),
            new HttpParameter("related", StringUtil.join(request.related)),
            new HttpParameter("lang", request.lang)
        };
        
        assertArrayEquals(expected, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        request1.setMaxWidth(800);
        request2.setMaxWidth(800);
        request1.setHideMedia(true);
        request2.setHideMedia(true);
        request1.setHideThread(true);
        request2.setHideThread(true);
        request1.setOmitScript(false);
        request2.setOmitScript(false);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request1.setRelated(related);
        request2.setRelated(related);
        request1.setLang("en");
        request2.setLang("en");
        
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        request1.setMaxWidth(800);
        request2.setMaxWidth(800);
        request1.setHideMedia(true);
        request2.setHideMedia(true);
        request1.setHideThread(true);
        request2.setHideThread(true);
        request1.setOmitScript(false);
        request2.setOmitScript(false);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request1.setRelated(related);
        request2.setRelated(related);
        request1.setLang("en");
        request2.setLang("en");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(800);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=800, hideMedia=true, hideThread=true, omitScript=false, align=RIGHT, related=[user1, user2], lang='en'}";
        
        assertEquals(expected, request.toString());
    }
}