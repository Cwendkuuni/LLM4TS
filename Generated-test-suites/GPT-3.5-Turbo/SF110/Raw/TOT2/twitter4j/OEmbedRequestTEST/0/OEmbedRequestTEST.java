import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(true);
        assertTrue(request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(true);
        assertTrue(request.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(false);
        assertFalse(request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request1, request2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        int expectedHashCode = 31 * (31 * (31 * (31 * (31 * (31 * (31 * 12345 + "https://example.com".hashCode()) + 0) + 1) + 1) + 0) + OEmbedRequest.Align.NONE.hashCode()) + 0;
        assertEquals(expectedHashCode, request.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String expectedString = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expectedString, request.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        HttpParameter[] params = request.asHttpParameterArray();
        assertNotNull(params);
        // Add additional assertions based on the implementation of asHttpParameterArray()
    }
}