import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(123456789, "http://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest maxWidthRequest = oEmbedRequest.MaxWidth(500);
        assertEquals(500, maxWidthRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest hideMediaRequest = oEmbedRequest.HideMedia(false);
        assertFalse(hideMediaRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest hideThreadRequest = oEmbedRequest.HideThread(false);
        assertFalse(hideThreadRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest omitScriptRequest = oEmbedRequest.omitScript(true);
        assertTrue(omitScriptRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest alignRequest = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, alignRequest.align);
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
        OEmbedRequest relatedRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, relatedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest langRequest = oEmbedRequest.lang("en");
        assertEquals("en", langRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] parameters = oEmbedRequest.asHttpParameterArray();

        assertEquals(9, parameters.length);
        assertEquals(new HttpParameter("id", "123456789"), parameters[0]);
        assertEquals(new HttpParameter("url", "http://example.com"), parameters[1]);
        assertEquals(new HttpParameter("maxwidth", 0), parameters[2]);
        assertEquals(new HttpParameter("hide_media", true), parameters[3]);
        assertEquals(new HttpParameter("hide_thread", true), parameters[4]);
        assertEquals(new HttpParameter("omit_script", false), parameters[5]);
        assertEquals(new HttpParameter("align", "none"), parameters[6]);
        assertEquals(new HttpParameter("related", "user1,user2"), parameters[7]);
        assertEquals(new HttpParameter("lang", "en"), parameters[8]);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "http://example.com");
        assertEquals(oEmbedRequest, oEmbedRequest1);
    }

    @Test
    public void testEqualsNotSame() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(987654321, "http://test.com");
        assertNotEquals(oEmbedRequest, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "http://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest1.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "OEmbedRequest{statusId=123456789, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedToString, oEmbedRequest.toString());
    }

}