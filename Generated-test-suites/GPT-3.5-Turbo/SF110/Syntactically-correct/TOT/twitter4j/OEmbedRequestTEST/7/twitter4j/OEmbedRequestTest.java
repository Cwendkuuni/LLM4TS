import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        assertEquals(123, oEmbedRequest.statusId);
        assertEquals("https://twitter.com/tweet", oEmbedRequest.url);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertArrayEquals(new String[0], oEmbedRequest.related);
        assertNull(oEmbedRequest.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "123"),
            new HttpParameter("url", "https://twitter.com/tweet"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "tweet1,tweet2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }
    
    @Test
    public void testEqualsSameObject() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }
    
    @Test
    public void testEqualsNullObject() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        assertFalse(oEmbedRequest.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        assertFalse(oEmbedRequest.equals("not a OEmbedRequest object"));
    }
    
    @Test
    public void testEqualsDifferentHideMedia() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest2.setHideMedia(false);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentHideThread() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest2.setHideThread(false);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentMaxWidth() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest2.setMaxWidth(500);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentOmitScript() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest2.setOmitScript(true);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentStatusId() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(456, "https://twitter.com/tweet");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentAlign() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentLang() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest2.setLang("en");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentRelated() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        String[] related = {"tweet1", "tweet2"};
        oEmbedRequest2.setRelated(related);
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsDifferentUrl() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/other_tweet");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsEqualObjectsWithNullLang() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123, "https://twitter.com/tweet");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://twitter.com/tweet");
        oEmbedRequest1.setLang(null);
        oEmbedRequest2.setLang(null);
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        int expectedHashCode = 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 123 + "https://twitter.com/tweet".hashCode()) + 0) + 1) + 1) + 0) + 0) + OEmbedRequest.Align.NONE.hashCode()) + Arrays.hashCode(new String[0])) + 0;
        assertEquals(expectedHashCode, oEmbedRequest.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123, "https://twitter.com/tweet");
        String expectedString = "OEmbedRequest{statusId=123, url='https://twitter.com/tweet', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }
}