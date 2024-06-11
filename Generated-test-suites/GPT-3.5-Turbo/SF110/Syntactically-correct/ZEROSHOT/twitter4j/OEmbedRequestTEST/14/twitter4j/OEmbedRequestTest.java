import org.junit.Assert;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        Assert.assertEquals(500, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setHideMedia(true);
        Assert.assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(true);
        Assert.assertTrue(result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setHideThread(true);
        Assert.assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(true);
        Assert.assertTrue(result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        Assert.assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        Assert.assertTrue(result.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        Assert.assertEquals(OEmbedRequest.Align.RIGHT, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        Assert.assertEquals(OEmbedRequest.Align.RIGHT, result.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setRelated(new String[]{"user1", "user2"});
        Assert.assertArrayEquals(new String[]{"user1", "user2"}, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.related(new String[]{"user1", "user2"});
        Assert.assertArrayEquals(new String[]{"user1", "user2"}, result.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        oEmbedRequest.setLang("en");
        Assert.assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("en");
        Assert.assertEquals("en", result.lang);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456, "https://example.com");
        Assert.assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        Assert.assertEquals(123456 ^ (123456 >>> 32), oEmbedRequest.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456, "https://example.com");
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        Assert.assertEquals(expected, oEmbedRequest.toString());
    }
}