import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
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
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"twitter", "social"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    // Test edge cases and error scenarios

    @Test
    public void testAppendParameterWithStringValue() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("name", "value", params);
        assertEquals(1, params.size());
    }

    @Test
    public void testAppendParameterWithLongValue() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        List<HttpParameter> params = new ArrayList<>();
        request.appendParameter("name", 100L, params);
        assertEquals(1, params.size());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        assertEquals(123456, request.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());
    }
}