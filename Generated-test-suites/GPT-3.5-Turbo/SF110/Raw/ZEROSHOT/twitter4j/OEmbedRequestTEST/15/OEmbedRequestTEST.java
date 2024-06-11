import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private long statusId;
    private String url;
    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        statusId = 12345L;
        url = "https://example.com";
        oEmbedRequest = new OEmbedRequest(statusId, url);
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(200);
        assertEquals(200, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertFalse(result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertFalse(result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(false);
        assertFalse(result.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest.Align align = OEmbedRequest.Align.LEFT;
        oEmbedRequest.setAlign(align);
        assertEquals(align, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        OEmbedRequest result = oEmbedRequest.align(align);
        assertEquals(align, result.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"related3", "related4"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        String lang = "en";
        oEmbedRequest.setLang(lang);
        assertEquals(lang, oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        String lang = "fr";
        OEmbedRequest result = oEmbedRequest.lang(lang);
        assertEquals(lang, result.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expected = new HttpParameter[12];
        expected[0] = new HttpParameter("id", statusId);
        expected[1] = new HttpParameter("url", url);
        expected[2] = new HttpParameter("maxwidth", oEmbedRequest.maxWidth);
        expected[3] = new HttpParameter("hide_media", oEmbedRequest.hideMedia);
        expected[4] = new HttpParameter("hide_thread", oEmbedRequest.hideThread);
        expected[5] = new HttpParameter("omit_script", oEmbedRequest.omitScript);
        expected[6] = new HttpParameter("align", oEmbedRequest.align.name().toLowerCase());
        expected[7] = new HttpParameter("related", StringUtil.join(oEmbedRequest.related));
        expected[8] = new HttpParameter("lang", oEmbedRequest.lang);

        HttpParameter[] actual = oEmbedRequest.asHttpParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(oEmbedRequest.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(oEmbedRequest.equals("test"));
    }

    @Test
    public void testEqualsDifferentHideMedia() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setHideMedia(false);
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentHideThread() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setHideThread(false);
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentMaxWidth() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setMaxWidth(200);
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentOmitScript() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setOmitScript(true);
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentStatusId() {
        OEmbedRequest other = new OEmbedRequest(statusId + 1, url);
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentAlign() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setAlign(OEmbedRequest.Align.CENTER);
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentLang() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setLang("fr");
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentRelated() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        other.setRelated(new String[]{"related1"});
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsDifferentUrl() {
        OEmbedRequest other = new OEmbedRequest(statusId, url + "/test");
        assertFalse(oEmbedRequest.equals(other));
    }

    @Test
    public void testEqualsSameValues() {
        OEmbedRequest other = new OEmbedRequest(statusId, url);
        assertTrue(oEmbedRequest.equals(other));
    }

    @Test
    public void testHashCode() {
        int result = oEmbedRequest.hashCode();

        int expected = (int)(statusId ^ (statusId >>> 32));
        expected = 31 * expected + ((url != null) ? url.hashCode() : 0);
        expected = 31 * expected + oEmbedRequest.maxWidth;
        expected = 31 * expected + (oEmbedRequest.hideMedia ? 1 : 0);
        expected = 31 * expected + (oEmbedRequest.hideThread ? 1 : 0);
        expected = 31 * expected + (oEmbedRequest.omitScript ? 1 : 0);
        expected = 31 * expected + ((oEmbedRequest.align != null) ? oEmbedRequest.align.hashCode() : 0);
        expected = 31 * expected + ((oEmbedRequest.related != null) ? Arrays.hashCode(oEmbedRequest.related) : 0);
        expected = 31 * expected + ((oEmbedRequest.lang != null) ? oEmbedRequest.lang.hashCode() : 0);

        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        String result = oEmbedRequest.toString();
        String expected = "OEmbedRequest{statusId=" + statusId + ", url='" + url + '\'' + ", maxWidth=" + oEmbedRequest.maxWidth + ", hideMedia=" + oEmbedRequest.hideMedia + ", hideThread=" + oEmbedRequest.hideThread + ", omitScript=" + oEmbedRequest.omitScript + ", align=" + oEmbedRequest.align + ", related=" + ((oEmbedRequest.related == null) ? null : Arrays.asList(oEmbedRequest.related)) + ", lang='" + oEmbedRequest.lang + '\'' + '}';
        
        assertEquals(expected, result);
    }

}