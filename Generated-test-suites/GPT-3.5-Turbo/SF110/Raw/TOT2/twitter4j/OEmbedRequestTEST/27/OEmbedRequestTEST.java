import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");

        assertEquals(12345L, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertEquals(0, oEmbedRequest.related.length);
        assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);

        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);

        assertEquals(500, result.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setHideMedia(false);

        assertFalse(oEmbedRequest.hideMedia);
    }

    // Add more test cases for other methods
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(54321L, "https://example.com");

        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");

        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");

        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, " +
                "omitScript=false, align=NONE, related=[], lang='null'}", oEmbedRequest.toString());
    }
}