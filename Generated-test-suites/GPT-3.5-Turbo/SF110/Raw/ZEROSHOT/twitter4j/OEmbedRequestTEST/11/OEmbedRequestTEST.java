import static org.junit.Assert.*;
import org.junit.*;

public class OEmbedRequestTest {
    
    private OEmbedRequest oEmbedRequest;
    
    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
    }
    
    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(100);
        assertEquals(100, result.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertEquals(true, oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(true);
        assertEquals(true, result.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertEquals(true, oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(true);
        assertEquals(true, result.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(false);
        assertEquals(false, oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(false);
        assertEquals(false, result.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, result.align);
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
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expected = {
            new HttpParameter("id", "1234"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "100"),
            new HttpParameter("hide_media", true),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "right"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234, "https://example.com");
        assertEquals(true, oEmbedRequest.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}