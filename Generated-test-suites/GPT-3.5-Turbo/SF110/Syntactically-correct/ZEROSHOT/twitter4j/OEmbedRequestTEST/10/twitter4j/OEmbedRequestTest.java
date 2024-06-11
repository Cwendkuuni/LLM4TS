import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    private OEmbedRequest oEmbedRequest;
    
    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(123456789, "https://www.example.com");
    }
    
    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, result.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(true);
        assertTrue(result.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(true);
        assertTrue(result.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertTrue(result.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.align);
    }
    
    @Test
    public void testSetRelated() {
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.related);
    }
    
    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest result = oEmbedRequest.lang("en");
        assertEquals("en", result.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expected = {
            new HttpParameter("id", "123456789"),
            new HttpParameter("url", "https://www.example.com"),
            new HttpParameter("maxwidth", 0),
            new HttpParameter("hide_media", true),
            new HttpParameter("hide_thread", true),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "none"),
            new HttpParameter("lang", null)
        };
        HttpParameter[] result = oEmbedRequest.asHttpParameterArray();
        assertArrayEquals(expected, result);
    }
    
    @After
    public void tearDown() {
        oEmbedRequest = null;
    }
}