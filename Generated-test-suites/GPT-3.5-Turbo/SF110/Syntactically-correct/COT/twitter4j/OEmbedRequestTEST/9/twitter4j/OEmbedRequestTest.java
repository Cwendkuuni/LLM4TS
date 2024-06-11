
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Validate the instance variables
        assertEquals(123456789, oembed.statusId);
        assertEquals("https://www.example.com", oembed.url);
        assertEquals(0, oembed.maxWidth);
        assertTrue(oembed.hideMedia);
        assertTrue(oembed.hideThread);
        assertFalse(oembed.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oembed.align);
        assertArrayEquals(new String[0], oembed.related);
        assertNull(oembed.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set the max width
        oembed.setMaxWidth(500);
        
        // Validate the max width
        assertEquals(500, oembed.maxWidth);
    }
    
    @Test
    public void testMaxWidth() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set the max width using the builder pattern
        oembed = oembed.maxWidth(500);
        
        // Validate the max width
        assertEquals(500, oembed.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set hide media
        oembed.setHideMedia(false);
        
        // Validate hide media
        assertFalse(oembed.hideMedia);
    }
    
    @Test
    public void testHideMedia() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set hide media using the builder pattern
        oembed = oembed.HideMedia(false);
        
        // Validate hide media
        assertFalse(oembed.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set hide thread
        oembed.setHideThread(false);
        
        // Validate hide thread
        assertFalse(oembed.hideThread);
    }
    
    @Test
    public void testHideThread() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set hide thread using the builder pattern
        oembed = oembed.HideThread(false);
        
        // Validate hide thread
        assertFalse(oembed.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set omit script
        oembed.setOmitScript(true);
        
        // Validate omit script
        assertTrue(oembed.omitScript);
    }
    
    @Test
    public void testOmitScript() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set omit script using the builder pattern
        oembed = oembed.omitScript(true);
        
        // Validate omit script
        assertTrue(oembed.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set align
        oembed.setAlign(OEmbedRequest.Align.CENTER);
        
        // Validate align
        assertEquals(OEmbedRequest.Align.CENTER, oembed.align);
    }
    
    @Test
    public void testAlign() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set align using the builder pattern
        oembed = oembed.align(OEmbedRequest.Align.CENTER);
        
        // Validate align
        assertEquals(OEmbedRequest.Align.CENTER, oembed.align);
    }
    
    @Test
    public void testSetRelated() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set related
        oembed.setRelated(new String[] {"twitter", "tweet"});
        
        // Validate related
        assertArrayEquals(new String[] {"twitter", "tweet"}, oembed.related);
    }
    
    @Test
    public void testRelated() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set related using the builder pattern
        oembed = oembed.related(new String[] {"twitter", "tweet"});
        
        // Validate related
        assertArrayEquals(new String[] {"twitter", "tweet"}, oembed.related);
    }
    
    @Test
    public void testSetLang() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set lang
        oembed.setLang("en");
        
        // Validate lang
        assertEquals("en", oembed.lang);
    }
    
    @Test
    public void testLang() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        
        // Set lang using the builder pattern
        oembed = oembed.lang("en");
        
        // Validate lang
        assertEquals("en", oembed.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        oembed.setMaxWidth(500);
        oembed.setHideMedia(false);
        oembed.setHideThread(false);
        oembed.setOmitScript(true);
        oembed.setAlign(OEmbedRequest.Align.CENTER);
        oembed.setRelated(new String[] {"twitter", "tweet"});
        oembed.setLang("en");
        
        // Get HttpParameter array
        HttpParameter[] params = oembed.asHttpParameterArray();
        
        // Validate the HttpParameter array
        assertEquals(12, params.length);
        assertEquals("id", params[0].getName());
        assertEquals("123456789", params[0].getValue());
        assertEquals("url", params[1].getName());
        assertEquals("https://www.example.com", params[1].getValue());
        assertEquals("maxwidth", params[2].getName());
        assertEquals("500", params[2].getValue());
        assertEquals("hide_media", params[3].getName());
        assertEquals("false", params[3].getValue());
        assertEquals("hide_thread", params[4].getName());
        assertEquals("false", params[4].getValue());
        assertEquals("omit_script", params[5].getName());
        assertEquals("true", params[5].getValue());
        assertEquals("align", params[6].getName());
        assertEquals("center", params[6].getValue());
        assertEquals("related", params[7].getName());
        assertEquals("twitter,tweet", params[7].getValue());
        assertEquals("lang", params[8].getName());
        assertEquals("en", params[8].getValue());
    }
    
    @Test
    public void testEquals() {
        // Create OEmbedRequest objects with same values
        OEmbedRequest oembed1 = new OEmbedRequest(123456789, "https://www.example.com");
        oembed1.setMaxWidth(500);
        oembed1.setHideMedia(false);
        oembed1.setHideThread(false);
        oembed1.setOmitScript(true);
        oembed1.setAlign(OEmbedRequest.Align.CENTER);
        oembed1.setRelated(new String[] {"twitter", "tweet"});
        oembed1.setLang("en");
        
        OEmbedRequest oembed2 = new OEmbedRequest(123456789, "https://www.example.com");
        oembed2.setMaxWidth(500);
        oembed2.setHideMedia(false);
        oembed2.setHideThread(false);
        oembed2.setOmitScript(true);
        oembed2.setAlign(OEmbedRequest.Align.CENTER);
        oembed2.setRelated(new String[] {"twitter", "tweet"});
        oembed2.setLang("en");
        
        // Validate equals method
        assertTrue(oembed1.equals(oembed2));
    }
    
    @Test
    public void testHashCode() {
        // Create OEmbedRequest objects with same values
        OEmbedRequest oembed1 = new OEmbedRequest(123456789, "https://www.example.com");
        oembed1.setMaxWidth(500);
        oembed1.setHideMedia(false);
        oembed1.setHideThread(false);
        oembed1.setOmitScript(true);
        oembed1.setAlign(OEmbedRequest.Align.CENTER);
        oembed1.setRelated(new String[] {"twitter", "tweet"});
        oembed1.setLang("en");
        
        OEmbedRequest oembed2 = new OEmbedRequest(123456789, "https://www.example.com");
        oembed2.setMaxWidth(500);
        oembed2.setHideMedia(false);
        oembed2.setHideThread(false);
        oembed2.setOmitScript(true);
        oembed2.setAlign(OEmbedRequest.Align.CENTER);
        oembed2.setRelated(new String[] {"twitter", "tweet"});
        oembed2.setLang("en");
        
        // Validate hashCode method
        assertEquals(oembed1.hashCode(), oembed2.hashCode());
    }
    
    @Test
    public void testToString() {
        // Create OEmbedRequest object
        OEmbedRequest oembed = new OEmbedRequest(123456789, "https://www.example.com");
        oembed.setMaxWidth(500);
        oembed.setHideMedia(false);
        oembed.setHideThread(false);
        oembed.setOmitScript(true);
        oembed.setAlign(OEmbedRequest.Align.CENTER);
        oembed.setRelated(new String[] {"twitter", "tweet"});
        oembed.setLang("en");
        
        // Validate toString method
        String expected = "OEmbedRequest{statusId=123456789, url='https://www.example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[twitter, tweet], lang='en'}";
        assertEquals(expected, oembed.toString());
    }
    
}
