
package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").MaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").HideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").HideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").omitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
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
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").related(new String[]{"user1", "user2"});
        assertArrayEquals(new String[]{"user1", "user2"}, request.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com").lang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] expectedParams = {
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", 500),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", OEmbedRequest.Align.CENTER.name().toLowerCase()),
                new HttpParameter("related", "user1,user2"),
                new HttpParameter("lang", "en")
        };

        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        String expectedToString = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=500, hideMedia=true, hideThread=true, omitScript=false, align=CENTER, related=[user1, user2], lang='en'}";

        assertEquals(expectedToString, request.toString());
    }

}
