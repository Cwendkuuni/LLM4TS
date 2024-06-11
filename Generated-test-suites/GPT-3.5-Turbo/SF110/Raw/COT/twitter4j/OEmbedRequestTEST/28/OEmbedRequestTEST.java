
import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        assertEquals(1234, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertEquals(true, oEmbedRequest.hideMedia);
        assertEquals(true, oEmbedRequest.hideThread);
        assertEquals(false, oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        assertEquals(0, oEmbedRequest.related.length);
        assertNull(oEmbedRequest.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        assertEquals(false, oEmbedRequest.hideMedia);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        assertEquals(false, oEmbedRequest.hideThread);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        assertEquals(true, oEmbedRequest.omitScript);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        String[] related = {"test1", "test2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        String[] related = {"test1", "test2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        assertArrayEquals(related, oEmbedRequest.related);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setLang("english");
        assertEquals("english", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("english");
        assertEquals("english", oEmbedRequest.lang);
        assertSame(oEmbedRequest, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"test1", "test2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("english");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "1234"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "test1,test2"),
            new HttpParameter("lang", "english")
        };
        
        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }
    
    // Test edge cases and exception handling scenarios
    
    @Test
    public void testAppendParameterWhenValueIsNull() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, null);
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("id", "1234", params);
        assertEquals(1, params.size());
        assertEquals("1234", params.get(0).getValue());
    }
    
    @Test
    public void testAppendParameterWhenValueIsNotNull() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, null);
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("url", "https://example.com", params);
        assertEquals(1, params.size());
        assertEquals("https://example.com", params.get(0).getValue());
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.CENTER);
        String[] related1 = {"test1", "test2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("english");
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        String[] related2 = {"test1", "test2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("english");
        
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }
    
    @Test
    public void testEqualsWhenEqualToSameInstance() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }
    
    @Test
    public void testEqualsWithNull() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        assertFalse(oEmbedRequest.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        assertFalse(oEmbedRequest.equals(new Object()));
    }
    
    @Test
    public void testEqualsWhenNotEqual() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(5678, "https://example.com");
        assertFalse(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testEqualsWhenEqual() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest1.setMaxWidth(500);
        oEmbedRequest1.setHideMedia(false);
        oEmbedRequest1.setHideThread(false);
        oEmbedRequest1.setOmitScript(true);
        oEmbedRequest1.setAlign(OEmbedRequest.Align.CENTER);
        String[] related1 = {"test1", "test2"};
        oEmbedRequest1.setRelated(related1);
        oEmbedRequest1.setLang("english");
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest2.setMaxWidth(500);
        oEmbedRequest2.setHideMedia(false);
        oEmbedRequest2.setHideThread(false);
        oEmbedRequest2.setOmitScript(true);
        oEmbedRequest2.setAlign(OEmbedRequest.Align.CENTER);
        String[] related2 = {"test1", "test2"};
        oEmbedRequest2.setRelated(related2);
        oEmbedRequest2.setLang("english");
        
        assertTrue(oEmbedRequest1.equals(oEmbedRequest2));
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"test1", "test2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("english");
        
        String expectedToString = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[test1, test2], lang='english'}";
        
        assertEquals(expectedToString, oEmbedRequest.toString());
    }
    
}
