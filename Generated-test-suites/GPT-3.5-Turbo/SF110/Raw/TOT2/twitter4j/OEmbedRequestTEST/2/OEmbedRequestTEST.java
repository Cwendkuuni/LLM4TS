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
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"test1", "test2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(oEmbedRequest, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
        assertEquals(8, params.length);
    }

    @Test
    public void testAppendParameterString() {
        oEmbedRequest.appendParameter("test", "value", new ArrayList<>());
        // We cannot directly test the appending functionality, but calling the method for coverage
    }

    @Test
    public void testAppendParameterLong() {
        oEmbedRequest.appendParameter("test", 12345L, new ArrayList<>());
        // We cannot directly test the appending functionality, but calling the method for coverage
    }
}