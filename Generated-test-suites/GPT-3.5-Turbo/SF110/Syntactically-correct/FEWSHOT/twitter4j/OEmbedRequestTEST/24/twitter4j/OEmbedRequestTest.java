package twitter4j;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OEmbedRequestTest {
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        Assert.assertEquals(12345L, oEmbedRequest.statusId);
        Assert.assertEquals("https://example.com", oEmbedRequest.url);
        Assert.assertEquals(0, oEmbedRequest.maxWidth);
        Assert.assertTrue(oEmbedRequest.hideMedia);
        Assert.assertTrue(oEmbedRequest.hideThread);
        Assert.assertFalse(oEmbedRequest.omitScript);
        Assert.assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
        Assert.assertArrayEquals(new String[0], oEmbedRequest.related);
        Assert.assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.MaxWidth(500);
        Assert.assertEquals(500, oEmbedRequest.maxWidth);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setHideMedia(false);
        Assert.assertFalse(oEmbedRequest.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideMedia(false);
        Assert.assertFalse(oEmbedRequest.hideMedia);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setHideThread(false);
        Assert.assertFalse(oEmbedRequest.hideThread);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.HideThread(false);
        Assert.assertFalse(oEmbedRequest.hideThread);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setOmitScript(true);
        Assert.assertTrue(oEmbedRequest.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.omitScript(true);
        Assert.assertTrue(oEmbedRequest.omitScript);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        Assert.assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.align(OEmbedRequest.Align.CENTER);
        Assert.assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        Assert.assertArrayEquals(related, oEmbedRequest.related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        String[] related = {"related1", "related2"};
        OEmbedRequest result = oEmbedRequest.related(related);
        Assert.assertArrayEquals(related, oEmbedRequest.related);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setLang("en");
        Assert.assertEquals("en", oEmbedRequest.lang);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest result = oEmbedRequest.lang("en");
        Assert.assertEquals("en", oEmbedRequest.lang);
        Assert.assertEquals(oEmbedRequest, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        HttpParameter[] expectedParams = {new HttpParameter("id", "12345"), new HttpParameter("url", "https://example.com"), 
                                           new HttpParameter("maxwidth", "500"), new HttpParameter("hide_media", false), 
                                           new HttpParameter("hide_thread", false), new HttpParameter("omit_script", true), 
                                           new HttpParameter("align", "center"), new HttpParameter("related", "related1 related2"),
                                           new HttpParameter("lang", "en")};
                                           
        HttpParameter[] actualParams = oEmbedRequest.asHttpParameterArray();
        
        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345L, "https://example.com");
        Assert.assertEquals(oEmbedRequest1, oEmbedRequest2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(12345L, "https://example.com");
        Assert.assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(12345L, "https://example.com");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        
        String expectedString = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[related1, related2], lang='en'}";
        String actualString = oEmbedRequest.toString();
        
        Assert.assertEquals(expectedString, actualString);
    }
}