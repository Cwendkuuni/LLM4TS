import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(200);
        int result = oEmbedRequest.getMaxWidth();
        assertEquals(200, result);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest result = oEmbedRequest.MaxWidth(300);
        assertEquals(300, result.getMaxWidth());
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        boolean result = oEmbedRequest.isHideMedia();
        assertTrue(result);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertFalse(result.isHideMedia());
    }
    
    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        boolean result = oEmbedRequest.isHideThread();
        assertTrue(result);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertFalse(result.isHideThread());
    }
    
    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        boolean result = oEmbedRequest.isOmitScript();
        assertTrue(result);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest result = oEmbedRequest.omitScript(false);
        assertFalse(result.isOmitScript());
    }
    
    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        OEmbedRequest.Align result = oEmbedRequest.getAlign();
        assertEquals(OEmbedRequest.Align.LEFT, result);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, result.getAlign());
    }
    
    @Test
    public void testSetRelated() {
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        String[] result = oEmbedRequest.getRelated();
        assertArrayEquals(related, result);
    }
    
    @Test
    public void testRelated() {
        String[] related = {"related3", "related4"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, result.getRelated());
    }
    
    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        String result = oEmbedRequest.getLang();
        assertEquals("en", result);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest result = oEmbedRequest.lang("fr");
        assertEquals("fr", result.getLang());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(200);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] result = oEmbedRequest.asHttpParameterArray();
        
        assertNotNull(result);
        assertEquals(9, result.length);
        assertEquals("id", result[0].getName());
        assertEquals("https://example.com", result[1].getValue());
        assertEquals("maxwidth", result[2].getName());
        assertEquals("200", result[2].getValue());
        assertEquals("hide_media", result[3].getName());
        assertEquals("false", result[3].getValue());
        assertEquals("hide_thread", result[4].getName());
        assertEquals("true", result[4].getValue());
        assertEquals("omit_script", result[5].getName());
        assertEquals("false", result[5].getValue());
        assertEquals("align", result[6].getName());
        assertEquals("center", result[6].getValue());
        assertEquals("related", result[7].getName());
        assertEquals("related1 related2", result[7].getValue());
        assertEquals("lang", result[8].getName());
        assertEquals("en", result[8].getValue());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345L, "https://example.com");
        assertTrue(oEmbedRequest.equals(oEmbedRequest2));
    }
    
    @Test
    public void testNotEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(54321L, "https://example.com");
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345L, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}