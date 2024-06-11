
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequest() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        assertEquals(123456789, request.statusId);
        assertEquals("https://twitter.com/twitter/status/123456789", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setMaxWidth(300);
        assertEquals(300, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest updatedRequest = request.MaxWidth(300);
        assertEquals(300, updatedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest updatedRequest = request.HideMedia(false);
        assertFalse(updatedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest updatedRequest = request.HideThread(false);
        assertFalse(updatedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest updatedRequest = request.omitScript(true);
        assertTrue(updatedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, updatedRequest.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        String[] related = {"user1", "user2"};
        OEmbedRequest updatedRequest = request.related(related);
        assertArrayEquals(related, updatedRequest.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest updatedRequest = request.lang("fr");
        assertEquals("fr", updatedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setMaxWidth(300);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(11, params.length);
        assertEquals(new HttpParameter("id", "123456789"), params[0]);
        assertEquals(new HttpParameter("url", "https://twitter.com/twitter/status/123456789"), params[1]);
        assertEquals(new HttpParameter("maxwidth", 300), params[2]);
        assertEquals(new HttpParameter("hide_media", false), params[3]);
        assertEquals(new HttpParameter("hide_thread", false), params[4]);
        assertEquals(new HttpParameter("omit_script", true), params[5]);
        assertEquals(new HttpParameter("align", "center"), params[6]);
        assertEquals(new HttpParameter("related", "user1,user2"), params[7]);
        assertEquals(new HttpParameter("lang", "en"), params[8]);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        request.setMaxWidth(300);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=123456789, url='https://twitter.com/twitter/status/123456789', maxWidth=300, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        assertEquals(expected, request.toString());
    }
}
