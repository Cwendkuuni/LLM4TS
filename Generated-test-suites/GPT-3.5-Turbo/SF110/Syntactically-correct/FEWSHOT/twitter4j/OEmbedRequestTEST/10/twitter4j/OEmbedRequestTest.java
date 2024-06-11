
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        assertEquals(1234, request.statusId);
        assertEquals("https://example.com", request.url);
        assertEquals(0, request.maxWidth);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertEquals(0, request.related.length);
        assertNull(request.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setMaxWidth(400);
        assertEquals(400, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.MaxWidth(500);
        assertEquals(500, updatedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.HideMedia(false);
        assertFalse(updatedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.HideThread(false);
        assertFalse(updatedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.omitScript(true);
        assertTrue(updatedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, updatedRequest.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setRelated(new String[]{"user1", "user2"});
        assertArrayEquals(new String[]{"user1", "user2"}, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.related(new String[]{"user1", "user2"});
        assertArrayEquals(new String[]{"user1", "user2"}, updatedRequest.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest updatedRequest = request.lang("fr");
        assertEquals("fr", updatedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        HttpParameter[] parameters = request.asHttpParameterArray();

        assertEquals(9, parameters.length);
        assertEquals("id", parameters[0].getName());
        assertEquals("1234", parameters[0].getValue());

        assertEquals("url", parameters[1].getName());
        assertEquals("https://example.com", parameters[1].getValue());

        assertEquals("maxwidth", parameters[2].getName());
        assertEquals("0", parameters[2].getValue());

        assertEquals("hide_media", parameters[3].getName());
        assertEquals("true", parameters[3].getValue());

        assertEquals("hide_thread", parameters[4].getName());
        assertEquals("true", parameters[4].getValue());

        assertEquals("omit_script", parameters[5].getName());
        assertEquals("false", parameters[5].getValue());

        assertEquals("align", parameters[6].getName());
        assertEquals("none", parameters[6].getValue());

        assertEquals("related", parameters[7].getName());
        assertEquals("", parameters[7].getValue());

        assertEquals("lang", parameters[8].getName());
        assertNull(parameters[8].getValue());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1234, "https://example.com");

        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(1234, "https://example.com");

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(1234, "https://example.com");
        String expected = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";

        assertEquals(expected, request.toString());
    }
}
