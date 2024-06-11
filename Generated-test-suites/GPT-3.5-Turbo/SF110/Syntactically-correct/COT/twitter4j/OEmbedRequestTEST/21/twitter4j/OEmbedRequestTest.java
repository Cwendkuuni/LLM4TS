
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    private OEmbedRequest oEmbedRequest;

    @Before
    public void setup() {
        oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest updatedRequest = oEmbedRequest.MaxWidth(500);
        assertEquals(500, updatedRequest.getMaxWidth());
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.isHideMedia());
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest updatedRequest = oEmbedRequest.HideMedia(true);
        assertTrue(updatedRequest.isHideMedia());
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.isHideThread());
    }

    @Test
    public void testHideThread() {
        OEmbedRequest updatedRequest = oEmbedRequest.HideThread(true);
        assertTrue(updatedRequest.isHideThread());
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest updatedRequest = oEmbedRequest.omitScript(true);
        assertTrue(updatedRequest.isOmitScript());
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.getAlign());
    }

    @Test
    public void testAlign() {
        OEmbedRequest updatedRequest = oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, updatedRequest.getAlign());
    }

    @Test
    public void testSetRelated() {
        String[] related = {"abc", "def"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
    }

    @Test
    public void testRelated() {
        String[] related = {"abc", "def"};
        OEmbedRequest updatedRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, updatedRequest.getRelated());
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());
    }

    @Test
    public void testLang() {
        OEmbedRequest updatedRequest = oEmbedRequest.lang("en");
        assertEquals("en", updatedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParamArray = {
            new HttpParameter("id", 123456789),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 0),
            new HttpParameter("hide_media", true),
            new HttpParameter("hide_thread", true),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "none"),
            new HttpParameter("lang", null)
        };
        assertArrayEquals(expectedParamArray, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest sameRequest = new OEmbedRequest(123456789, "https://example.com");
        assertTrue(oEmbedRequest.equals(sameRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest sameRequest = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), sameRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedToString, oEmbedRequest.toString());
    }
}