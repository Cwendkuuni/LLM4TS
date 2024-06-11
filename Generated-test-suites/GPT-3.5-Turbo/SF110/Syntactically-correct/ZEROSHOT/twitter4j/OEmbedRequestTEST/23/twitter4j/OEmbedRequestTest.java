package twitter4j;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OEmbedRequestTest {
    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest maxWidthRequest = oEmbedRequest.MaxWidth(800);
        assertEquals(800, maxWidthRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest hideMediaRequest = oEmbedRequest.HideMedia(false);
        assertEquals(false, hideMediaRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest hideThreadRequest = oEmbedRequest.HideThread(false);
        assertEquals(false, hideThreadRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest omitScriptRequest = oEmbedRequest.omitScript(true);
        assertEquals(true, omitScriptRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest alignRequest = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, alignRequest.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"related1", "related2", "related3"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"related1", "related2", "related3"};
        OEmbedRequest relatedRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, relatedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest langRequest = oEmbedRequest.lang("fr");
        assertEquals("fr", langRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2", "related3"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expectedParams = {
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "right"),
            new HttpParameter("related", "related1,related2,related3"),
            new HttpParameter("lang", "en")
        };

        HttpParameter[] actualParams = oEmbedRequest.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        OEmbedRequest sameRequest = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(true, oEmbedRequest.equals(sameRequest));

        OEmbedRequest differentRequest = new OEmbedRequest(9876543210L, "https://example2.com");
        assertEquals(false, oEmbedRequest.equals(differentRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest sameRequest = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), sameRequest.hashCode());

        OEmbedRequest differentRequest = new OEmbedRequest(9876543210L, "https://example2.com");
        assertNotEquals(oEmbedRequest.hashCode(), differentRequest.hashCode());
    }

    @Test
    public void testToString() {
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2", "related3"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        String expectedToString = "OEmbedRequest{statusId=1234567890, url='https://example.com', " +
            "maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, " +
            "align=RIGHT, related=[related1, related2, related3], lang='en'}";

        assertEquals(expectedToString, oEmbedRequest.toString());
    }
}