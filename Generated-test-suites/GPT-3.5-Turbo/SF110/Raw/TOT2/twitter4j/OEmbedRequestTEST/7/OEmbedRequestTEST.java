import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"abc", "123"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        request.setLang("en");
        String[] related = {"abc", "123"};
        request.setRelated(related);
        request.setHideMedia(false);
        
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(params.length, 8);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());
    }
}