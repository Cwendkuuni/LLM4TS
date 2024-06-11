import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(123456789, request.statusId);
        assertEquals("https://example.com", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
        
        request.MaxWidth(300);
        assertEquals(300, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
        
        request.HideMedia(true);
        assertTrue(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        request.setHideThread(false);
        assertFalse(request.hideThread);
        
        request.HideThread(true);
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        request.setOmitScript(true);
        assertTrue(request.omitScript);
        
        request.omitScript(false);
        assertFalse(request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
        
        request.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        String[] related = {"user1", "user2", "user3"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
        
        related = new String[]{"user4", "user5"};
        request.related(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        request.setLang("en");
        assertEquals("en", request.lang);
        
        request.lang("es");
        assertEquals("es", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "123456789"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 500),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", OEmbedRequest.Align.RIGHT.name().toLowerCase()),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest request3 = new OEmbedRequest(987654321, "https://example2.com");
        
        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest request3 = new OEmbedRequest(987654321, "https://example2.com");
        
        assertTrue(request1.hashCode() == request2.hashCode());
        assertFalse(request1.hashCode() == request3.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(300);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");
        
        String expectedToString = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=300, hideMedia=false, hideThread=false, omitScript=true, align=left, related=[user1, user2], lang='en'}";
        
        assertEquals(expectedToString, request.toString());
    }
}