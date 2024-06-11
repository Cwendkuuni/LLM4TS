
import org.junit.Assert;
import org.junit.Test;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        Assert.assertEquals(12345, oEmbedRequest.statusId);
        Assert.assertEquals("https://example.com", oEmbedRequest.url);
        Assert.assertEquals(true, oEmbedRequest.hideMedia);
        Assert.assertEquals(true, oEmbedRequest.hideThread);
        Assert.assertEquals(false, oEmbedRequest.omitScript);
        Assert.assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        Assert.assertArrayEquals(new String[0], oEmbedRequest.related);
        Assert.assertEquals(null, oEmbedRequest.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setMaxWidth(500);
        
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setHideMedia(false);
        
        Assert.assertEquals(false, oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        
        Assert.assertEquals(false, oEmbedRequest.hideMedia);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setHideThread(false);
        
        Assert.assertEquals(false, oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        
        Assert.assertEquals(false, oEmbedRequest.hideThread);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setOmitScript(true);
        
        Assert.assertEquals(true, oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        
        Assert.assertEquals(true, oEmbedRequest.omitScript);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        
        Assert.assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        
        Assert.assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        
        Assert.assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        
        Assert.assertArrayEquals(related, oEmbedRequest.related);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setLang("en");
        
        Assert.assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        OEmbedRequest result = oEmbedRequest.lang("en");
        
        Assert.assertEquals("en", oEmbedRequest.lang);
        Assert.assertEquals(oEmbedRequest, result);
    }
    
    // Additional Test Cases for Edge Cases and Exception Handling
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest.setRelated(new String[] {"related1", "related2"});
        oEmbedRequest.setLang("en");
        
        HttpParameter[] result = oEmbedRequest.asHttpParameterArray();
        
        Assert.assertEquals(12, result.length);
        // add assertions for each parameter
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345, "https://example.com");
        
        boolean result = oEmbedRequest1.equals(oEmbedRequest2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_SameObject() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        boolean result = oEmbedRequest.equals(oEmbedRequest);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_NullObject() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        boolean result = oEmbedRequest.equals(null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentClass() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345, "https://example.com");
        
        boolean result = oEmbedRequest.equals("test");
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentHideMedia() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345, "https://example.com");
        oEmbedRequest1.setHideMedia(false);
        
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345, "https://example.com");
        
        boolean result = oEmbedRequest1.equals(oEmbedRequest2);
        
        Assert.assertFalse(result);
    }
    
    // Additional test cases for other attributes and scenarios
    
}

