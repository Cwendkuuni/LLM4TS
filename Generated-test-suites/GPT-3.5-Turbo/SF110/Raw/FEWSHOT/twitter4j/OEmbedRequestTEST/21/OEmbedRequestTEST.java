
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(123, request.statusId);
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
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        assertEquals(200, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.MaxWidth(300);
        assertEquals(300, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideMedia(false);
        assertEquals(false, request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.HideMedia(false);
        assertEquals(false, result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideThread(false);
        assertEquals(false, request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.HideThread(false);
        assertEquals(false, result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setOmitScript(true);
        assertEquals(true, request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.omitScript(true);
        assertEquals(true, result.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        OEmbedRequest result = request.related(related);
        assertArrayEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.lang("es");
        assertEquals("es", result.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] expectedParams = new HttpParameter[9];
        expectedParams[0] = new HttpParameter("id", "123");
        expectedParams[1] = new HttpParameter("url", "https://example.com");
        expectedParams[2] = new HttpParameter("maxwidth", "200");
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", "right");
        expectedParams[7] = new HttpParameter("related", "user1,user2");
        expectedParams[8] = new HttpParameter("lang", "en");

        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        request1.setMaxWidth(200);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related1 = {"user1", "user2"};
        request1.setRelated(related1);
        request1.setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        request2.setMaxWidth(200);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related2 = {"user1", "user2"};
        request2.setRelated(related2);
        request2.setLang("en");

        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        request1.setMaxWidth(200);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related1 = {"user1", "user2"};
        request1.setRelated(related1);
        request1.setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        request2.setMaxWidth(200);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related2 = {"user1", "user2"};
        request2.setRelated(related2);
        request2.setLang("en");

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        String expectedToString = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=RIGHT, related=[user1, user2], lang='en'}";

        assertEquals(expectedToString, request.toString());
    }
}
