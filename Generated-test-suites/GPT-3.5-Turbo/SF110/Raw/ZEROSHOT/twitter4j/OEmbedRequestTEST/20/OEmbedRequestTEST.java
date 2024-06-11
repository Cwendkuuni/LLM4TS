import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(200);
        assertEquals(200, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(300);
        assertEquals(300, oEmbedRequest.maxWidth);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, oEmbedRequest.align);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"tweet3", "tweet4"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.related);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest result = oEmbedRequest.lang("fr");
        assertEquals("fr", oEmbedRequest.lang);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expected = {
            new HttpParameter("id", "1234"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", 200),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "right"),
            new HttpParameter("related", "tweet1,tweet2"),
            new HttpParameter("lang", "en")
        };

        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest1.setMaxWidth(200);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest2.setMaxWidth(200);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest1.setMaxWidth(200);
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest2.setMaxWidth(200);
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}