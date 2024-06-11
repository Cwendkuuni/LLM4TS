import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
    }

    @Test
    public void testMaxWidth() {
        oEmbedRequest.setMaxWidth(800);
        assertEquals(800, oEmbedRequest.getMaxWidth());
        assertEquals(oEmbedRequest, oEmbedRequest.maxWidth(800));
    }

    @Test
    public void testHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.isHideMedia());
        assertEquals(oEmbedRequest, oEmbedRequest.hideMedia(false));
    }

    @Test
    public void testHideThread() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.isHideThread());
        assertEquals(oEmbedRequest, oEmbedRequest.hideThread(false));
    }

    @Test
    public void testOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.isOmitScript());
        assertEquals(oEmbedRequest, oEmbedRequest.omitScript(true));
    }

    @Test
    public void testAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.getAlign());
        assertEquals(oEmbedRequest, oEmbedRequest.align(OEmbedRequest.Align.CENTER));
    }

    @Test
    public void testRelated() {
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());
        assertEquals(oEmbedRequest, oEmbedRequest.related(related));
    }

    @Test
    public void testLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());
        assertEquals(oEmbedRequest, oEmbedRequest.lang("en"));
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expected = {
                new HttpParameter("id", "1234"),
                new HttpParameter("url", "https://example.com"),
                new HttpParameter("maxwidth", 0),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "none"),
                new HttpParameter("lang", null)
        };
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest anotherOEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        assertTrue(oEmbedRequest.equals(anotherOEmbedRequest));

        anotherOEmbedRequest = new OEmbedRequest(5678, "https://example.org");
        assertFalse(oEmbedRequest.equals(anotherOEmbedRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest anotherOEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), anotherOEmbedRequest.hashCode());

        anotherOEmbedRequest = new OEmbedRequest(5678, "https://example.org");
        assertNotEquals(oEmbedRequest.hashCode(), anotherOEmbedRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=0, hideMedia=true, " +
                "hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}