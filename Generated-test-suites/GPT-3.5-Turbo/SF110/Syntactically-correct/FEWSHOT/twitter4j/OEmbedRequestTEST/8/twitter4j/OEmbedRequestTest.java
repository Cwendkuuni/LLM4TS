
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        assertNotNull(request);
        assertEquals(12345L, request.statusId);
        assertEquals("https://twitter.com/twitterapi/status/12345", request.url);
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
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request.setMaxWidth(400);
        assertEquals(400, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest newRequest = request.MaxWidth(400);
        assertEquals(request, newRequest);
        assertEquals(400, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest newRequest = request.HideMedia(false);
        assertEquals(request, newRequest);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest newRequest = request.HideThread(false);
        assertEquals(request, newRequest);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest newRequest = request.omitScript(true);
        assertEquals(request, newRequest);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest newRequest = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(request, newRequest);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        String[] related = {"twitterapi", "twitter"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        String[] related = {"twitterapi", "twitter"};
        OEmbedRequest newRequest = request.related(related);
        assertEquals(request, newRequest);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest newRequest = request.lang("en");
        assertEquals(request, newRequest);
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        String[] related = {"twitterapi", "twitter"};
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "12345"),
            new HttpParameter("url", "https://twitter.com/twitterapi/status/12345"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", true),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "twitterapi,twitter"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest request3 = new OEmbedRequest(54321L, "https://twitter.com/twitterapi/status/54321");
        
        OEmbedRequest request4 = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request4.setMaxWidth(300);
        OEmbedRequest request5 = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        request5.setAlign(OEmbedRequest.Align.LEFT);
        
        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, new Object());
        assertNotEquals(request4, request1);
        assertNotEquals(request5, request1);
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://twitter.com/twitterapi/status/12345");
        String expected = "OEmbedRequest{statusId=12345, url='https://twitter.com/twitterapi/status/12345', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());
    }
}
