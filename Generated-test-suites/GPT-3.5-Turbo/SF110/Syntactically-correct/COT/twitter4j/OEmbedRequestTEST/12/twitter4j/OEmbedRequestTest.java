import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        assertEquals(123456789, oEmbedRequest.statusId);
        assertEquals("https://www.example.com", oEmbedRequest.url);
        assertEquals(true, oEmbedRequest.hideMedia);
        assertEquals(true, oEmbedRequest.hideThread);
        assertEquals(false, oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertEquals(0, oEmbedRequest.related.length);
        assertEquals(null, oEmbedRequest.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, result.maxWidth);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertEquals(false, result.hideMedia);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertEquals(false, result.hideThread);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertEquals(true, result.omitScript);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.related);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        OEmbedRequest result = oEmbedRequest.lang("en");
        assertEquals("en", result.lang);
        assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");

        HttpParameter[] expectedParams = new HttpParameter[12];
        expectedParams[0] = new HttpParameter("id", "123456789");
        expectedParams[1] = new HttpParameter("url", "https://www.example.com");
        expectedParams[2] = new HttpParameter("maxwidth", 500);
        expectedParams[3] = new HttpParameter("hide_media", false);
        expectedParams[4] = new HttpParameter("hide_thread", false);
        expectedParams[5] = new HttpParameter("omit_script", true);
        expectedParams[6] = new HttpParameter("align", "center");
        expectedParams[7] = new HttpParameter("related", "related1,related2");
        expectedParams[8] = new HttpParameter("lang", "en");
    
        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }
}