import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(800);
        assertEquals(800, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest updatedRequest = oEmbedRequest.MaxWidth(800);
        assertEquals(800, updatedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest updatedRequest = oEmbedRequest.HideMedia(false);
        assertFalse(updatedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest updatedRequest = oEmbedRequest.HideThread(false);
        assertFalse(updatedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest updatedRequest = oEmbedRequest.omitScript(true);
        assertTrue(updatedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest updatedRequest = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, updatedRequest.align);
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
        OEmbedRequest updatedRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, updatedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest updatedRequest = oEmbedRequest.lang("en");
        assertEquals("en", updatedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = {
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", 0),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "none"),
                new HttpParameter("lang", null)
        };
        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest sameRequest = new OEmbedRequest(12345L, "https://example.com");
        assertTrue(oEmbedRequest.equals(sameRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest sameRequest = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), sameRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=none, related=null, lang='null'}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }
}