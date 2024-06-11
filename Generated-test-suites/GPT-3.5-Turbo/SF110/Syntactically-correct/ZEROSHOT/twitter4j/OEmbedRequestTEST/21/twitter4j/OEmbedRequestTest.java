import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest result = request.MaxWidth(500);
        assertEquals(500, request.maxWidth);
        assertSame(request, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest result = request.HideMedia(true);
        assertTrue(request.hideMedia);
        assertSame(request, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest result = request.HideThread(true);
        assertTrue(request.hideThread);
        assertSame(request, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setOmitScript(false);
        assertFalse(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest result = request.omitScript(false);
        assertFalse(request.omitScript);
        assertSame(request, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest result = request.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
        assertSame(request, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest result = request.related(related);
        assertArrayEquals(related, request.related);
        assertSame(request, result);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest result = request.lang("en");
        assertEquals("en", request.lang);
        assertSame(request, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        HttpParameter[] result = request.asHttpParameterArray();
        assertNotNull(result);
        assertEquals(12, result.length);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "http://example.com");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        assertNotNull(request.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        assertNotNull(request.toString());
    }
}