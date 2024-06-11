import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(12345L, request.statusId);
        assertEquals("https://example.com", request.url);
        assertEquals(true, request.hideMedia);
        assertEquals(true, request.hideThread);
        assertEquals(false, request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest modifiedRequest = request.MaxWidth(500);
        assertEquals(500, modifiedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setHideMedia(false);
        assertEquals(false, request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest modifiedRequest = request.HideMedia(false);
        assertEquals(false, modifiedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setHideThread(false);
        assertEquals(false, request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest modifiedRequest = request.HideThread(false);
        assertEquals(false, modifiedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setOmitScript(true);
        assertEquals(true, request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest modifiedRequest = request.omitScript(true);
        assertEquals(true, modifiedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest modifiedRequest = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, modifiedRequest.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest modifiedRequest = request.related(related);
        assertArrayEquals(related, modifiedRequest.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest modifiedRequest = request.lang("en");
        assertEquals("en", modifiedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] expectedParams = {
            new HttpParameter("id", "12345"),
            new HttpParameter("url", "https://example.com"),
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
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(true, request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(194695335, request.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", request.toString());
    }
}