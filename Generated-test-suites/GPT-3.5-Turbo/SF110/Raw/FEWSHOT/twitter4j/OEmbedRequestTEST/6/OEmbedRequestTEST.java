package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OEmbedRequestTest {
    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(12345, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
        assertEquals(0, request.getMaxWidth());
        assertEquals(true, request.isHideMedia());
        assertEquals(true, request.isHideThread());
        assertEquals(false, request.isOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertEquals(null, request.getLang());
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.getMaxWidth());
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertEquals(false, request.isHideMedia());
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertEquals(false, request.isHideThread());
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertEquals(true, request.isOmitScript());
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.getAlign());
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = { "user1", "user2" };
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.getLang());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = { "user1", "user2" };
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = {
            new HttpParameter("id", "12345"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "500"),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "center"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(true, request1.equals(request2));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(-1217159289, request.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", request.toString());
    }
}