
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertNotNull(request);
        assertEquals(12345, request.statusId);
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
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.MaxWidth(500);
        assertEquals(500, request.maxWidth);
        assertSame(request, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.HideMedia(false);
        assertFalse(request.hideMedia);
        assertSame(request, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.HideThread(false);
        assertFalse(request.hideThread);
        assertSame(request, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.omitScript(true);
        assertTrue(request.omitScript);
        assertSame(request, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
        assertSame(request, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setRelated(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest result = request.related(new String[]{"related1", "related2"});
        assertArrayEquals(new String[]{"related1", "related2"}, request.related);
        assertSame(request, result);
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
        OEmbedRequest result = request.lang("en");
        assertEquals("en", request.lang);
        assertSame(request, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(12, params.length);

        assertEquals(new HttpParameter("id", "12345"), params[0]);
        assertEquals(new HttpParameter("url", "https://example.com"), params[1]);
        assertEquals(new HttpParameter("maxwidth", "500"), params[2]);
        assertEquals(new HttpParameter("hide_media", false), params[3]);
        assertEquals(new HttpParameter("hide_thread", false), params[4]);
        assertEquals(new HttpParameter("omit_script", true), params[5]);
        assertEquals(new HttpParameter("align", "right"), params[6]);
        assertEquals(new HttpParameter("related", "related1,related2"), params[7]);
        assertEquals(new HttpParameter("lang", "en"), params[8]);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        request1.setRelated(new String[]{"related1", "related2"});
        request1.setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        request2.setRelated(new String[]{"related1", "related2"});
        request2.setLang("en");

        assertTrue(request1.equals(request2));
        assertTrue(request2.equals(request1));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        request1.setRelated(new String[]{"related1", "related2"});
        request1.setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        request2.setRelated(new String[]{"related1", "related2"});
        request2.setLang("en");

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        request.setRelated(new String[]{"related1", "related2"});
        request.setLang("en");

        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=RIGHT, related=[related1, related2], lang='en'}", request.toString());
    }
}
