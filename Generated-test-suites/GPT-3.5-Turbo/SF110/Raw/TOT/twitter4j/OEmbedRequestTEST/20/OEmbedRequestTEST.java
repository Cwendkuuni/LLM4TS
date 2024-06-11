
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        assertEquals(12345L, request.statusId);
        assertEquals("https://example.com/my_tweet", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        HttpParameter[] expected = new HttpParameter[9];
        expected[0] = new HttpParameter("id", "12345");
        expected[1] = new HttpParameter("url", "https://example.com/my_tweet");
        expected[2] = new HttpParameter("maxwidth", "500");
        expected[3] = new HttpParameter("hide_media", false);
        expected[4] = new HttpParameter("hide_thread", false);
        expected[5] = new HttpParameter("omit_script", true);
        expected[6] = new HttpParameter("align", "center");
        expected[7] = new HttpParameter("related", "user1,user2");
        expected[8] = new HttpParameter("lang", "en");

        assertArrayEquals(expected, request.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        OEmbedRequest request3 = new OEmbedRequest(54321L, "https://example.com/another_tweet");

        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        OEmbedRequest request3 = new OEmbedRequest(54321L, "https://example.com/another_tweet");

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com/my_tweet");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=12345, url='https://example.com/my_tweet', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=center, related=[user1, user2], lang='en'}";

        assertEquals(expected, request.toString());
    }
}
