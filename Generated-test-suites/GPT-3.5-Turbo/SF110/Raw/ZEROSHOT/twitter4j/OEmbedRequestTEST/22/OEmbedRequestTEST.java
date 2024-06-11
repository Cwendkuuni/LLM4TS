package twitter4j;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    }

    @Test
    public void testMaxWidth() {
        oEmbedRequest.setMaxWidth(400);
        assertEquals(400, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidthBuilder() {
        OEmbedRequest newOEmbedRequest = oEmbedRequest.MaxWidth(600);
        assertEquals(600, newOEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMediaBuilder() {
        OEmbedRequest newOEmbedRequest = oEmbedRequest.HideMedia(false);
        assertFalse(newOEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThreadBuilder() {
        OEmbedRequest newOEmbedRequest = oEmbedRequest.HideThread(false);
        assertFalse(newOEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScriptBuilder() {
        OEmbedRequest newOEmbedRequest = oEmbedRequest.omitScript(false);
        assertFalse(newOEmbedRequest.omitScript);
    }

    @Test
    public void testAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }

    @Test
    public void testAlignBuilder() {
        OEmbedRequest newOEmbedRequest = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, newOEmbedRequest.align);
    }

    @Test
    public void testRelated() {
        String[] related = {"abc", "def"};
        oEmbedRequest.setRelated(related);
        assertTrue(Arrays.equals(related, oEmbedRequest.related));
    }

    @Test
    public void testRelatedBuilder() {
        String[] related = {"ghi", "jkl"};
        OEmbedRequest newOEmbedRequest = oEmbedRequest.related(related);
        assertTrue(Arrays.equals(related, newOEmbedRequest.related));
    }

    @Test
    public void testLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLangBuilder() {
        OEmbedRequest newOEmbedRequest = oEmbedRequest.lang("fr");
        assertEquals("fr", newOEmbedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(400);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"abc", "def"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        HttpParameter[] expectedParams = {new HttpParameter("id", "123456789"), new HttpParameter("url", "https://example.com"), new HttpParameter("maxwidth", 400), new HttpParameter("hide_media", false),
                new HttpParameter("hide_thread", true), new HttpParameter("omit_script", true), new HttpParameter("align", "left"), new HttpParameter("related", "abc,def"), new HttpParameter("lang", "en")};
        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(oEmbedRequest, oEmbedRequest1);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest1.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }
}