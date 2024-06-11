
import org.junit.Test;

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
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com").MaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com").HideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com").HideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com").omitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com").align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
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
        assertArrayEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com").lang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] expected = {
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "http://example.com"),
                new HttpParameter("maxwidth", "500"),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "center"),
                new HttpParameter("related", "user1,user2"),
                new HttpParameter("lang", "en")
        };
        assertArrayEquals(expected, request.asHttpParameterArray());
    }

    @Test
    public void testAppendParameterWithStringValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("test", "value", params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("test", "value"), params.get(0));
    }

    @Test
    public void testAppendParameterWithLongValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("test", 123, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("test", "123"), params.get(0));
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "http://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "http://example.com");
        assertEquals(request1, request2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        assertEquals(1071003551, request.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "http://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=12345, url='http://example.com', maxWidth=500, " +
                "hideMedia=true, hideThread=true, omitScript=false, align=CENTER, related=[user1, user2], " +
                "lang='en'}";
        assertEquals(expected, request.toString());
    }
}

