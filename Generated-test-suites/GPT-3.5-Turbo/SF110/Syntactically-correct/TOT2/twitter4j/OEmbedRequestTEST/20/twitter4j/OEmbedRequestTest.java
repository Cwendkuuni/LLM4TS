package twitter4j;
import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(400);
        assertEquals(400, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        oEmbedRequest.MaxWidth(600);
        assertEquals(600, oEmbedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        oEmbedRequest.HideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        oEmbedRequest.HideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }
    
    // Add more test cases for other methods as needed

}