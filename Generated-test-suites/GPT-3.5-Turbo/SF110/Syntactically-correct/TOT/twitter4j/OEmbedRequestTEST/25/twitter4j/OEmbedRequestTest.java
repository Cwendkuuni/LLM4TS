import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testConstructor() {
        OEmbedRequest oEmbed = new OEmbedRequest(1234567890L, "https://example.com");
        
        assertEquals(1234567890L, oEmbed.statusId);
        assertEquals("https://example.com", oEmbed.url);
        assertEquals(0, oEmbed.maxWidth);
        assertTrue(oEmbed.hideMedia);
        assertTrue(oEmbed.hideThread);
        assertFalse(oEmbed.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, oEmbed.align);
        assertArrayEquals(new String[0], oEmbed.related);
        assertNull(oEmbed.lang);
    }
    
    @Test
    public void testSetterMethods() {
        OEmbedRequest oEmbed = new OEmbedRequest(1234567890L, "https://example.com");
        
        oEmbed.setMaxWidth(500);
        assertEquals(500, oEmbed.maxWidth);
        
        oEmbed.setHideMedia(false);
        assertFalse(oEmbed.hideMedia);
        
        oEmbed.setHideThread(false);
        assertFalse(oEmbed.hideThread);
        
        oEmbed.setOmitScript(true);
        assertTrue(oEmbed.omitScript);
        
        oEmbed.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbed.align);
        
        String[] related = {"user1", "user2"};
        oEmbed.setRelated(related);
        assertArrayEquals(related, oEmbed.related);
        
        oEmbed.setLang("en");
        assertEquals("en", oEmbed.lang);
    }
    
    @Test
    public void testBuilderMethods() {
        OEmbedRequest oEmbed = new OEmbedRequest(1234567890L, "https://example.com");
        
        oEmbed.MaxWidth(500);
        assertEquals(500, oEmbed.maxWidth);
        
        oEmbed.HideMedia(false);
        assertFalse(oEmbed.hideMedia);
        
        oEmbed.HideThread(false);
        assertFalse(oEmbed.hideThread);
        
        oEmbed.omitScript(true);
        assertTrue(oEmbed.omitScript);
        
        oEmbed.align(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbed.align);
        
        String[] related = {"user1", "user2"};
        oEmbed.related(related);
        assertArrayEquals(related, oEmbed.related);
        
        oEmbed.lang("en");
        assertEquals("en", oEmbed.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbed = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbed.setMaxWidth(500);
        oEmbed.setHideMedia(false);
        oEmbed.setHideThread(false);
        oEmbed.setOmitScript(true);
        oEmbed.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbed.setRelated(related);
        oEmbed.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "1234567890"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, oEmbed.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest oEmbed1 = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest oEmbed2 = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest oEmbed3 = new OEmbedRequest(9876543210L, "https://example.com");
        OEmbedRequest oEmbed4 = new OEmbedRequest(1234567890L, "https://different.com");
        
        assertTrue(oEmbed1.equals(oEmbed2));
        assertFalse(oEmbed1.equals(oEmbed3));
        assertFalse(oEmbed1.equals(oEmbed4));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest oEmbed1 = new OEmbedRequest(1234567890L, "https://example.com");
        OEmbedRequest oEmbed2 = new OEmbedRequest(1234567890L, "https://example.com");
        
        assertEquals(oEmbed1.hashCode(), oEmbed2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest oEmbed = new OEmbedRequest(1234567890L, "https://example.com");
        oEmbed.setMaxWidth(500);
        oEmbed.setHideMedia(false);
        oEmbed.setHideThread(false);
        oEmbed.setOmitScript(true);
        oEmbed.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        oEmbed.setRelated(related);
        oEmbed.setLang("en");
        
        String expectedToString = "OEmbedRequest{statusId=1234567890, url='https://example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";
        
        assertEquals(expectedToString, oEmbed.toString());
    }
}