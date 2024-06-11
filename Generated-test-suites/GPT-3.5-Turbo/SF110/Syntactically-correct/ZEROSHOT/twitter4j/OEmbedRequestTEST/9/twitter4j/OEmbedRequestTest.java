import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(true);
        assertTrue(result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(true);
        assertTrue(result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertTrue(result.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("en");
        assertEquals("en", result.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expectedParams = new HttpParameter[12];
        expectedParams[0] = new HttpParameter("id", "1234567890");
        expectedParams[1] = new HttpParameter("url", "https://example.com");
        expectedParams[2] = new HttpParameter("maxwidth", 500);
        expectedParams[3] = new HttpParameter("hide_media", true);
        expectedParams[4] = new HttpParameter("hide_thread", true);
        expectedParams[5] = new HttpParameter("omit_script", false);
        expectedParams[6] = new HttpParameter("align", "center");
        expectedParams[7] = new HttpParameter("related", "related1,related2");
        expectedParams[8] = new HttpParameter("lang", "en");

        HttpParameter[] actualParams = oEmbedRequest.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890L, "https://example.com");
        assertTrue(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
        String expectedString = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }
}