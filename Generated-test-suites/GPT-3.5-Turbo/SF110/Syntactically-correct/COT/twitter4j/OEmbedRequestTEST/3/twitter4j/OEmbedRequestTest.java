package twitter4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oembedRequest;

    @Before
    public void setUp() {
        oembedRequest = new OEmbedRequest(123456789, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oembedRequest.setMaxWidth(500);
        assertEquals(500, oembedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oembedRequest.maxWidth(500);
        assertEquals(500, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oembedRequest.setHideMedia(true);
        assertTrue(oembedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oembedRequest.hideMedia(true);
        assertTrue(result.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oembedRequest.setHideThread(true);
        assertTrue(oembedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest result = oembedRequest.hideThread(true);
        assertTrue(result.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oembedRequest.setOmitScript(true);
        assertTrue(oembedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest result = oembedRequest.omitScript(true);
        assertTrue(result.omitScript);
    }

    @Test
    public void testSetAlign() {
        oembedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, oembedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest result = oembedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, result.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"user1", "user2"};
        oembedRequest.setRelated(related);
        assertArrayEquals(related, oembedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"user1", "user2"};
        OEmbedRequest result = oembedRequest.related(related);
        assertArrayEquals(related, result.related);
    }

    @Test
    public void testSetLang() {
        oembedRequest.setLang("en");
        assertEquals("en", oembedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest result = oembedRequest.lang("en");
        assertEquals("en", result.lang);
    }

    @Test
    public void testEquals() {
        OEmbedRequest sameRequest = new OEmbedRequest(123456789, "https://example.com");
        assertTrue(oembedRequest.equals(sameRequest));

        OEmbedRequest differentRequest = new OEmbedRequest(987654321, "https://example.com");
        assertFalse(oembedRequest.equals(differentRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest sameRequest = new OEmbedRequest(123456789, "https://example.com");
        assertEquals(oembedRequest.hashCode(), sameRequest.hashCode());

        OEmbedRequest differentRequest = new OEmbedRequest(987654321, "https://example.com");
        assertNotEquals(oembedRequest.hashCode(), differentRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oembedRequest.toString());
    }
}