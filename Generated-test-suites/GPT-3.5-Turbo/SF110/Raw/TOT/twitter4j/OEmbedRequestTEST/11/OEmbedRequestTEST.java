import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(12345, request.statusId);
        assertEquals("https://example.com", request.url);
        assertEquals(true, request.hideMedia);
        assertEquals(true, request.hideThread);
        assertEquals(false, request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
        assertNull(request.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request, request.MaxWidth(500));
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertEquals(false, request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request, request.HideMedia(false));
        assertEquals(false, request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertEquals(false, request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request, request.HideThread(false));
        assertEquals(false, request.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertEquals(true, request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request, request.omitScript(true));
        assertEquals(true, request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request, request.align(OEmbedRequest.Align.LEFT));
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        assertEquals(request, request.related(related));
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request, request.lang("en"));
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = new HttpParameter[12];
        
        expectedParams[0] = new HttpParameter("id", "12345");
        expectedParams[1] = new HttpParameter("url", "https://example.com");
        expectedParams[2] = new HttpParameter("maxwidth", "500");
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", "left");
        expectedParams[7] = new HttpParameter("related", "user1,user2");
        expectedParams[8] = new HttpParameter("lang", "en");
        
        HttpParameter[] actualParams = request.asHttpParameterArray();
        
        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        
        assertEquals(request1, request2);
    }

    @Test
    public void testNotEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        
        OEmbedRequest request2 = new OEmbedRequest(54321, "https://example.com");
        request2.setMaxWidth(500);
        
        assertNotEquals(request1, request2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", request.toString());
    }
}