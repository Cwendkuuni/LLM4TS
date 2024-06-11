import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(1234567890L, request.statusId);
        assertEquals("https://example.com", request.url);
        assertEquals(0, request.maxWidth);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest newRequest = request.maxWidth(500);
        assertEquals(500, newRequest.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest newRequest = request.HideMedia(false);
        assertFalse(newRequest.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest newRequest = request.HideThread(false);
        assertFalse(newRequest.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest newRequest = request.omitScript(true);
        assertTrue(newRequest.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest newRequest = request.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, newRequest.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest newRequest = request.related(related);
        assertArrayEquals(related, newRequest.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest newRequest = request.lang("en");
        assertEquals("en", newRequest.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setHideMedia(false);
        request.setMaxWidth(500);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", true),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "user1, user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1234567890L, "https://example.com");
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testNotEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(9876543210L, "https://example.com");
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1234567890L, "https://example.com");
        request.setLang("en");
        String expectedString = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='en'}";
        assertEquals(expectedString, request.toString());
    }

}