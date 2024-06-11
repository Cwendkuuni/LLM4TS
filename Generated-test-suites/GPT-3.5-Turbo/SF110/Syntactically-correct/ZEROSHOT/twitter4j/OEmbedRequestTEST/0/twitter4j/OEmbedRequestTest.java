import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    private OEmbedRequest oEmbedRequest;
    
    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(123456789, "https://example.com");
    }
    
    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest newRequest = oEmbedRequest.MaxWidth(500);
        assertEquals(500, newRequest.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest newRequest = oEmbedRequest.HideMedia(true);
        assertTrue(newRequest.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest newRequest = oEmbedRequest.HideThread(true);
        assertTrue(newRequest.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest newRequest = oEmbedRequest.omitScript(true);
        assertTrue(newRequest.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest newRequest = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, newRequest.align);
    }
    
    @Test
    public void testSetRelated() {
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        String[] related = {"user1", "user2"};
        OEmbedRequest newRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, newRequest.related);
    }
    
    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest newRequest = oEmbedRequest.lang("en");
        assertEquals("en", newRequest.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expected = new HttpParameter[12];
        expected[0] = new HttpParameter("id", "123456789");
        expected[1] = new HttpParameter("url", "https://example.com");
        expected[2] = new HttpParameter("maxwidth", 500);
        expected[3] = new HttpParameter("hide_media", true);
        expected[4] = new HttpParameter("hide_thread", false);
        expected[5] = new HttpParameter("omit_script", false);
        expected[6] = new HttpParameter("align", "right");
        expected[7] = new HttpParameter("related", "user1,user2");
        expected[8] = new HttpParameter("lang", "en");
        
        assertEquals(expected.length, oEmbedRequest.asHttpParameterArray().length);
        assertArrayEquals(expected, oEmbedRequest.asHttpParameterArray());
    }
    
    // Add more test methods to test all methods in the OEmbedRequest class
    
}