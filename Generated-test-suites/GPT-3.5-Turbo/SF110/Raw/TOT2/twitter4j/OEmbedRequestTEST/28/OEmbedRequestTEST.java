import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        assertEquals(12345, oEmbedRequest.statusId);
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
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.setMaxWidth(300);
        assertEquals(300, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest.MaxWidth(400);
        assertEquals(400, oEmbedRequest.maxWidth);
    }
    
    // Additional test cases for other methods should be added here

}