package twitter4j;

import org.junit.Assert;
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
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.maxWidth(500);
        Assert.assertEquals(500, result.maxWidth);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        Assert.assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.hideMedia(true);
        Assert.assertTrue(result.hideMedia);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        Assert.assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.hideThread(true);
        Assert.assertTrue(result.hideThread);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        Assert.assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        Assert.assertTrue(result.omitScript);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        Assert.assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        Assert.assertEquals(OEmbedRequest.Align.CENTER, result.align);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"news", "sports"};
        oEmbedRequest.setRelated(related);
        Assert.assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"news", "sports"};
        OEmbedRequest result = oEmbedRequest.related(related);
        Assert.assertArrayEquals(related, result.related);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        Assert.assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest result = oEmbedRequest.lang("en");
        Assert.assertEquals("en", result.lang);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"news", "sports"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expected = new HttpParameter[9];
        expected[0] = new HttpParameter("id", "123456789");
        expected[1] = new HttpParameter("url", "https://example.com");
        expected[2] = new HttpParameter("maxwidth", "500");
        expected[3] = new HttpParameter("hide_media", true);
        expected[4] = new HttpParameter("hide_thread", true);
        expected[5] = new HttpParameter("omit_script", false);
        expected[6] = new HttpParameter("align", "left");
        expected[7] = new HttpParameter("related", "news,sports");
        expected[8] = new HttpParameter("lang", "en");

        HttpParameter[] result = oEmbedRequest.asHttpParameterArray();

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testEquals() {
        OEmbedRequest sameObject = new OEmbedRequest(123456789, "https://example.com");
        OEmbedRequest differentObject = new OEmbedRequest(987654321, "https://example.com");
        differentObject.setMaxWidth(500);

        Assert.assertTrue(oEmbedRequest.equals(sameObject));
        Assert.assertFalse(oEmbedRequest.equals(differentObject));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest sameObject = new OEmbedRequest(123456789, "https://example.com");

        Assert.assertEquals(sameObject.hashCode(), oEmbedRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";

        Assert.assertEquals(expected, oEmbedRequest.toString());
    }
}