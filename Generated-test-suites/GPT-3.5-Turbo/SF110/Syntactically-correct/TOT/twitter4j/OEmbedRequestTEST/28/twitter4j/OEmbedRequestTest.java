package twitter4j;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    }

    @Test
    public void testMaxWidth() {
        int maxWidth = 500;
        oEmbedRequest.setMaxWidth(maxWidth);
        assertEquals(maxWidth, oEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        boolean hideMedia = false;
        oEmbedRequest.setHideMedia(hideMedia);
        assertEquals(hideMedia, oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        boolean hideThread = false;
        oEmbedRequest.setHideThread(hideThread);
        assertEquals(hideThread, oEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        boolean omitScript = true;
        oEmbedRequest.setOmitScript(omitScript);
        assertEquals(omitScript, oEmbedRequest.omitScript);
    }

    @Test
    public void testAlign() {
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.align);
    }

    @Test
    public void testRelated() {
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testLang() {
        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedArray = {
                new HttpParameter("id", "123456789"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", 0),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "none"),
                new HttpParameter("lang", null)
        };

        HttpParameter[] actualArray = oEmbedRequest.asHttpParameterArray();

        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testEquals() {
        OEmbedRequest equalRequest = new OEmbedRequest(123456789, "https://example.com");
        assertTrue(oEmbedRequest.equals(equalRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest equalRequest = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), equalRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }

}