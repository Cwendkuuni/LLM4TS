import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        oEmbedRequest = oEmbedRequest.MaxWidth(1000);
        Assert.assertEquals(1000, oEmbedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        Assert.assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        oEmbedRequest = oEmbedRequest.HideMedia(false);
        Assert.assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        Assert.assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        oEmbedRequest = oEmbedRequest.HideThread(false);
        Assert.assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        Assert.assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        oEmbedRequest = oEmbedRequest.omitScript(false);
        Assert.assertFalse(oEmbedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        Assert.assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        oEmbedRequest = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        Assert.assertEquals(OEmbedRequest.Align.RIGHT, oEmbedRequest.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        Assert.assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"user3", "user4"};
        oEmbedRequest = oEmbedRequest.related(related);
        Assert.assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        Assert.assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        oEmbedRequest = oEmbedRequest.lang("fr");
        Assert.assertEquals("fr", oEmbedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(800);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"user5", "user6"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("es");
        HttpParameter[] expected = {new HttpParameter("id", "12345"), new HttpParameter("url", "https://example.com"), new HttpParameter("maxwidth", "800"), new HttpParameter("hide_media", false), new HttpParameter("hide_thread", true), new HttpParameter("omit_script", true), new HttpParameter("align", "left"), new HttpParameter("related", "user5,user6"), new HttpParameter("lang", "es")};
        Assert.assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testAppendParameterWithValue() {
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("param1", "value1", params);
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("param1", params.get(0).getName());
        Assert.assertEquals("value1", params.get(0).getValue());
    }

    @Test
    public void testAppendParameterWithNullValue() {
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("param2", null, params);
        Assert.assertEquals(0, params.size());
    }

    @Test
    public void testAppendParameterWithPositiveValue() {
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("param3", 10L, params);
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("param3", params.get(0).getName());
        Assert.assertEquals("10", params.get(0).getValue());
    }

    @Test
    public void testAppendParameterWithNegativeValue() {
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("param4", -5L, params);
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("param4", params.get(0).getName());
        Assert.assertEquals("-5", params.get(0).getValue());
    }

    @Test
    public void testEqualsSameObject() {
        Assert.assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }

    @Test
    public void testEqualsNullObject() {
        Assert.assertFalse(oEmbedRequest.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Object obj = new Object();
        Assert.assertFalse(oEmbedRequest.equals(obj));
    }

    @Test
    public void testEqualsDifferentHideMedia() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setHideMedia(false);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentHideThread() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setHideThread(false);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentMaxWidth() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setMaxWidth(1000);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentOmitScript() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setOmitScript(true);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentStatusId() {
        OEmbedRequest other = new OEmbedRequest(54321L, "https://example.com");
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentAlign() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setAlign(OEmbedRequest.Align.CENTER);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentLang() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setLang("en");
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentRelated() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"user1", "user2"};
        other.setRelated(related);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentUrl() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.org");
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsEqualObjects() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        Assert.assertTrue(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsNullLangAndRelated() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setLang(null);
        other.setRelated(null);
        Assert.assertTrue(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsNullUrl() {
        OEmbedRequest other = new OEmbedRequest(12345L, null);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsNullUrlAndLang() {
        OEmbedRequest other = new OEmbedRequest(12345L, null);
        other.setLang(null);
        Assert.assertTrue(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsNullUrlAndNonNullLang() {
        OEmbedRequest other = new OEmbedRequest(12345L, null);
        other.setLang("en");
        Assert.assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsNonNullUrlAndNullLang() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        other.setLang(null);
        Assert.assertFalse(oEmbedRequest.equals(other));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest other = new OEmbedRequest(12345L, "https://example.com");
        Assert.assertEquals(oEmbedRequest.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        Assert.assertEquals(expected, oEmbedRequest.toString());
    }
}