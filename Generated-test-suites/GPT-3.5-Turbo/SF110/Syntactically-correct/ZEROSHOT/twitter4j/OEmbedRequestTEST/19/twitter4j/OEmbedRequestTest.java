import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class OEmbedRequestTest {

    @Test
    public void testConstructors() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        assertNotNull(request);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setMaxWidth(100);
        assertEquals(100, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.MaxWidth(200);
        assertEquals(200, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.HideMedia(false);
        assertFalse(result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.HideThread(false);
        assertFalse(result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.omitScript(false);
        assertFalse(result.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, result.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        String[] related = {"twitter", "example"};
        request.setRelated(related);
        assertEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        String[] related = {"twitter", "example"};
        OEmbedRequest result = request.related(related);
        assertEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.lang("fr");
        assertEquals("fr", result.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        String[] related = {"twitter", "example"};
        request.setRelated(related);
        HttpParameter[] params = request.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(10, params.length);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://www.example.com");
        request1.setAlign(OEmbedRequest.Align.LEFT);
        
        OEmbedRequest request2 = new OEmbedRequest(54321L, "https://www.example.com");
        request2.setAlign(OEmbedRequest.Align.LEFT);
        
        OEmbedRequest request3 = new OEmbedRequest(12345L, "https://www.example.com");
        request3.setAlign(OEmbedRequest.Align.RIGHT);
        
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://www.example.com");
        request1.setAlign(OEmbedRequest.Align.LEFT);
        
        OEmbedRequest request2 = new OEmbedRequest(54321L, "https://www.example.com");
        request2.setAlign(OEmbedRequest.Align.LEFT);
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        String result = "OEmbedRequest{statusId=12345, url='https://www.example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(result, request.toString());
    }
}