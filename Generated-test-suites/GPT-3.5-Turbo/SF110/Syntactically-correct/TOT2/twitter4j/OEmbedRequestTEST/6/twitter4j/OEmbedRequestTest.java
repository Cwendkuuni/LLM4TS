package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(400);
        assertEquals(400, request.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        assertEquals(request1, request2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        int hash = request.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, request.toString());
    }

    // Additional test cases for edge cases and exception handling can be added here

}