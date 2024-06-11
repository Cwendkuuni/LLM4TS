import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    private OEmbedRequest oEmbedRequest;
    
    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
    }
    
    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testHideMedia() {
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideThread() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }
    
    @Test
    public void testOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }
    
    @Test
    public void testAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
    }
    
    @Test
    public void testRelated() {
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = oEmbedRequest.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(9, params.length);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345L, "https://example.com");
        assertTrue(oEmbedRequest.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = oEmbedRequest.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbedRequest.toString());
    }
}