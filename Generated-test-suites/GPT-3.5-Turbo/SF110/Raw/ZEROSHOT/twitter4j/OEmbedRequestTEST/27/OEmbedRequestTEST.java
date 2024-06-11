package twitter4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(1234567890L, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(true);
        assertTrue(result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(true);
        assertTrue(result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertTrue(result.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = new String[]{"related1", "related2"};
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
        oEmbedRequest.setLang("en");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);

        HttpParameter[] expected = new HttpParameter[7];
        expected[0] = new HttpParameter("id", "1234567890");
        expected[1] = new HttpParameter("url", "https://example.com");
        expected[2] = new HttpParameter("maxwidth", "500");
        expected[3] = new HttpParameter("hide_media", true);
        expected[4] = new HttpParameter("hide_thread", true);
        expected[5] = new HttpParameter("omit_script", false);
        expected[6] = new HttpParameter("align", "center");
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(oEmbedRequest, oEmbedRequest2);
    }

    @Test
    public void testNotEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(9876543210L, "https://example.org");
        assertNotEquals(oEmbedRequest, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234567890L, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        oEmbedRequest.setLang("en");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = new String[]{"related1", "related2"};
        oEmbedRequest.setRelated(related);

        String expected = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=500, " +
                "hideMedia=true, hideThread=true, omitScript=false, align=CENTER, related=[related1, related2], lang='en'}";

        assertEquals(expected, oEmbedRequest.toString());
    }
}