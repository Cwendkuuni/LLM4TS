import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        assertEquals(200, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(request, request.MaxWidth(200));
        assertEquals(200, request.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(request, request.HideMedia(false));
        assertFalse(request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(request, request.HideThread(false));
        assertFalse(request.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(request, request.omitScript(true));
        assertTrue(request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setAlign(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(request, request.align(OEmbedRequest.Align.RIGHT));
        assertEquals(OEmbedRequest.Align.RIGHT, request.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"related1", "related2"};
        assertEquals(request, request.related(related));
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        assertEquals(request, request.lang("en"));
        assertEquals("en", request.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        HttpParameter[] parameters = request.asHttpParameterArray();
        assertEquals(12, parameters.length);
        // Check the values of each parameter
        // TODO: Add assertions for each parameter value
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123, "https://example.com");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        int hashCode = (int) (123 ^ (123 >>> 32)) // Calculate expected hash code based on input values
                + ((request.url != null) ? request.url.hashCode() : 0)
                + 31 * 200
                + 31 * (request.hideMedia ? 1 : 0)
                + 31 * (request.hideThread ? 1 : 0)
                + 31 * (request.omitScript ? 1 : 0)
                + ((request.align != null) ? request.align.hashCode() : 0)
                + ((request.related != null) ? Arrays.hashCode(request.related) : 0)
                + ((request.lang != null) ? request.lang.hashCode() : 0);
        assertEquals(hashCode, request.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(200);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        String expectedString = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=200, hideMedia=false, hideThread=false, omitScript=true, align=RIGHT, related=[related1, related2], lang='en'}";
        assertEquals(expectedString, request.toString());
    }

}