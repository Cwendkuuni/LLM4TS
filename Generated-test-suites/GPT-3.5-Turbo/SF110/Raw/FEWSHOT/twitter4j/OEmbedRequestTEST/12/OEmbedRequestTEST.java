package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(200);
        assertEquals(200, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(200);
        assertEquals(200, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertEquals(false, result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertEquals(false, result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertEquals(true, result.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, result.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"user1", "user2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest result = oEmbedRequest.lang("en");
        assertEquals("en", result.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expected = {new HttpParameter("id", "123456"), new HttpParameter("url", "https://example.com"), new HttpParameter("maxwidth", 0), 
            new HttpParameter("hide_media", true), new HttpParameter("hide_thread", true), new HttpParameter("omit_script", false), 
            new HttpParameter("align", "none"), new HttpParameter("lang", null)};
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456, "https://example.com");
        assertEquals(oEmbedRequest, oEmbedRequest2);
        oEmbedRequest2.setMaxWidth(200);
        assertNotEquals(oEmbedRequest, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
        oEmbedRequest2.setMaxWidth(200);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}