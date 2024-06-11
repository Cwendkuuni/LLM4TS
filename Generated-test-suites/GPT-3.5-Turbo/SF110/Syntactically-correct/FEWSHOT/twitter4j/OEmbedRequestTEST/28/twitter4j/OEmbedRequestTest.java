package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OEmbedRequestTest {

    private final long statusId = 1234L;
    private final String url = "https://example.com";

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals(statusId, oEmbedRequest.getStatusId());
        assertEquals(url, oEmbedRequest.getUrl());
        assertEquals(0, oEmbedRequest.getMaxWidth());
        assertTrue(oEmbedRequest.isHideMedia());
        assertTrue(oEmbedRequest.isHideThread());
        assertFalse(oEmbedRequest.isOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.getAlign());
        assertEquals(0, oEmbedRequest.getRelated().length);
        assertEquals(null, oEmbedRequest.getLang());
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(300);
        assertEquals(300, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        OEmbedRequest response = oEmbedRequest.maxWidth(300);
        assertEquals(300, response.getMaxWidth());
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        OEmbedRequest response = oEmbedRequest.hideMedia(false);
        assertFalse(response.isHideMedia());
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        OEmbedRequest response = oEmbedRequest.hideThread(false);
        assertFalse(response.isHideThread());
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        OEmbedRequest response = oEmbedRequest.omitScript(true);
        assertTrue(response.isOmitScript());
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.getAlign());
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        OEmbedRequest response = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, response.getAlign());
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        assertEquals(2, oEmbedRequest.getRelated().length);
        assertEquals("user1", oEmbedRequest.getRelated()[0]);
        assertEquals("user2", oEmbedRequest.getRelated()[1]);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        String[] related = {"user1", "user2"};
        OEmbedRequest response = oEmbedRequest.related(related);
        assertEquals(2, response.getRelated().length);
        assertEquals("user1", response.getRelated()[0]);
        assertEquals("user2", response.getRelated()[1]);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        OEmbedRequest response = oEmbedRequest.lang("en");
        assertEquals("en", response.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        oEmbedRequest.setMaxWidth(300);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expectedParams = new HttpParameter[10];
        expectedParams[0] = new HttpParameter("id", String.valueOf(statusId));
        expectedParams[1] = new HttpParameter("url", url);
        expectedParams[2] = new HttpParameter("maxwidth", 300);
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", OEmbedRequest.Align.CENTER.name().toLowerCase());
        expectedParams[7] = new HttpParameter("related", StringUtil.join(related));
        expectedParams[8] = new HttpParameter("lang", "en");
        expectedParams[9] = null;

        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testAppendParameterWithStringValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        ArrayList<HttpParameter> params = new ArrayList<>();
        String name = "name";
        String value = "value";
        oEmbedRequest.appendParameter(name, value, params);
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(value, params.get(0).getValue());
    }

    @Test
    public void testAppendParameterWithLongValue() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        ArrayList<HttpParameter> params = new ArrayList<>();
        String name = "name";
        long value = 12345;
        oEmbedRequest.appendParameter(name, value, params);
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(String.valueOf(value), params.get(0).getValue());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        oEmbedRequest1.setHideMedia(false);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
        oEmbedRequest2.setHideMedia(false);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testEqualsWithDifferentHideMedia() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        oEmbedRequest1.setHideMedia(false);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
        oEmbedRequest2.setHideMedia(true);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testEqualsWithDifferentStatusId() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(5678L, url);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testEqualsWithDifferentUrl() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, "https://example2.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        oEmbedRequest1.setHideMedia(false);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
        oEmbedRequest2.setHideMedia(false);
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentHideMedia() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(statusId, url);
        oEmbedRequest1.setHideMedia(false);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(statusId, url);
        oEmbedRequest2.setHideMedia(true);
        assertFalse(oEmbedRequest1.hashCode() == oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(statusId, url);
        assertEquals("OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", oEmbedRequest.toString());
    }
}