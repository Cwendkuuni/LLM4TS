
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setOmitScript(false);
        assertFalse(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testMaxWidthBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .MaxWidth(800);
        assertEquals(800, request.maxWidth);
    }
    
    @Test
    public void testHideMediaBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .HideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testHideThreadBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .HideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testOmitScriptBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .omitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testAlignBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    @Test
    public void testRelatedBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .related(new String[] {"user1", "user2"});
        assertArrayEquals(new String[] {"user1", "user2"}, request.related);
    }
    
    @Test
    public void testLangBuilder() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com")
                .lang("en");
        assertEquals("en", request.lang);
    }
}
