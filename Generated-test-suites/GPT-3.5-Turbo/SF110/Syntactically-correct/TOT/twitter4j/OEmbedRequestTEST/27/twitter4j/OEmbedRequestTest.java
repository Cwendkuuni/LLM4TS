import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
  
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        assertEquals(1234567890, request.statusId);
        assertEquals("https://www.example.com", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
        assertNull(request.lang);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setRelated(new String[] {"user1", "user2"});
        assertArrayEquals(new String[] {"user1", "user2"}, request.related);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[] {"user1", "user2"});
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://www.example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.CENTER);
        request1.setRelated(new String[] {"user1", "user2"});
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.CENTER);
        request2.setRelated(new String[] {"user1", "user2"});
        request2.setLang("en");
        
        assertTrue(request1.equals(request2));
    }
    
    @Test
    public void testEquals_sameObject() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        
        assertTrue(request.equals(request));
    }
    
    @Test
    public void testEquals_differentClass() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        Object obj = new Object();
        
        assertFalse(request.equals(obj));
    }
    
    @Test
    public void testEquals_null() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        
        assertFalse(request.equals(null));
    }
    
    @Test
    public void testEquals_differentHideMedia() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setHideMedia(true);
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setHideMedia(false);
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentHideThread() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setHideThread(true);
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setHideThread(false);
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentMaxWidth() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setMaxWidth(500);
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setMaxWidth(700);
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentOmitScript() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setOmitScript(true);
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setOmitScript(false);
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentStatusId() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        
        OEmbedRequest request2 = new OEmbedRequest(9876543210, "https://www.example.com");
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentAlign() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setAlign(OEmbedRequest.Align.LEFT);
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentLang() {
        OEmbedRequest request1 = new OEmbedRequest(1234567890, "https://www.example.com");
        request1.setLang("en");
        
        OEmbedRequest request2 = new OEmbedRequest(1234567890, "https://www.example.com");
        request2.setLang("es");
        
        assertFalse(request1.equals(request2));
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1234567890, "https://www.example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[] {"user1", "user2"});
        request.setLang("en");
        
        String expected = "OEmbedRequest{statusId=1234567890, url='https://www.example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        
        assertEquals(expected, request.toString());
    }

}