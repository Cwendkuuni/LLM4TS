package twitter4j;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testEquals() {
        OEmbedRequest other = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        assertTrue(oEmbedRequest.equals(other));
    }

    @Test
    public void testNotEquals() {
        OEmbedRequest other = new OEmbedRequest(54321, "https://twitter.com/example/status/54321");
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest other = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        assertEquals(oEmbedRequest.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=12345, url='https://twitter.com/example/status/12345', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest other = new OEmbedRequest(12345, "https://twitter.com/example/status/12345");
        HttpParameter[] expected = new HttpParameter[]{
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "https://twitter.com/example/status/12345"),
                new HttpParameter("maxwidth", 0),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "none"),
                new HttpParameter("lang", null)
        };
        assertArrayEquals(expected, other.asHttpParameterArray());
    }
}