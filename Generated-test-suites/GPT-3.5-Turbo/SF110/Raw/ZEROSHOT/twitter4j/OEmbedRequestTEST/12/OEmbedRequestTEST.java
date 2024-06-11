import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class OEmbedRequestTest {

    private static final long STATUS_ID = 123456;
    private static final String URL = "https://example.com";
    
    private OEmbedRequest oEmbedRequest;
    
    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(STATUS_ID, URL);
    }
    
    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(200);
        assertEquals(200, result.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertEquals(false, result.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertEquals(false, result.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertEquals(true, result.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, result.align);
    }
    
    @Test
    public void testSetRelated() {
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        assertEquals(Arrays.asList(related), Arrays.asList(oEmbedRequest.related));
    }
    
    @Test
    public void testRelated() {
        String[] related = {"related3", "related4"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertEquals(Arrays.asList(related), Arrays.asList(result.related));
    }
    
    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest result = oEmbedRequest.lang("fr");
        assertEquals("fr", result.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expected = {
            new HttpParameter("id", String.valueOf(STATUS_ID)),
            new HttpParameter("url", URL),
            new HttpParameter("maxwidth", 100),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", OEmbedRequest.Align.RIGHT.name().toLowerCase()),
            new HttpParameter("related", StringUtil.join(related)),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest equalRequest = new OEmbedRequest(STATUS_ID, URL);
        equalRequest.setMaxWidth(100);
        equalRequest.setHideMedia(false);
        equalRequest.setHideThread(false);
        equalRequest.setOmitScript(true);
        equalRequest.setAlign(OEmbedRequest.Align.RIGHT);
        
        String[] related = {"related1", "related2"};
        equalRequest.setRelated(related);
        
        equalRequest.setLang("en");
        
        assertTrue(oEmbedRequest.equals(equalRequest));
    }
    
    @Test
    public void testNotEquals() {
        OEmbedRequest differentRequest = new OEmbedRequest(STATUS_ID, URL);
        differentRequest.setMaxWidth(200);
        
        assertFalse(oEmbedRequest.equals(differentRequest));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest equalRequest = new OEmbedRequest(STATUS_ID, URL);
        equalRequest.setMaxWidth(100);
        equalRequest.setHideMedia(false);
        equalRequest.setHideThread(false);
        equalRequest.setOmitScript(true);
        equalRequest.setAlign(OEmbedRequest.Align.RIGHT);
        
        String[] related = {"related1", "related2"};
        equalRequest.setRelated(related);
        
        equalRequest.setLang("en");
        
        assertEquals(oEmbedRequest.hashCode(), equalRequest.hashCode());
    }
    
    @Test
    public void testToString() {
        oEmbedRequest.setMaxWidth(100);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        
        oEmbedRequest.setLang("en");
        
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=100, hideMedia=false, hideThread=false, omitScript=true, align=RIGHT, related=[related1, related2], lang='en'}";
        
        assertEquals(expected, oEmbedRequest.toString());
    }
}