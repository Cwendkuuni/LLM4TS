import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {
    
    @Test
    public void testOEmbedRequest() {
        // Typical use cases
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(12345, request1.statusId);
        assertEquals("https://example.com", request1.url);
        assertTrue(request1.hideMedia);
        assertTrue(request1.hideThread);
        assertFalse(request1.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request1.align);
        assertArrayEquals(new String[0], request1.related);
        assertNull(request1.lang);
        
        // Edge case
        OEmbedRequest request2 = new OEmbedRequest(0, "");
        assertEquals(0, request2.statusId);
        assertEquals("", request2.url);
        assertTrue(request2.hideMedia);
        assertTrue(request2.hideThread);
        assertFalse(request2.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request2.align);
        assertArrayEquals(new String[0], request2.related);
        assertNull(request2.lang);
    }
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
        
        // Edge case
        request.setMaxWidth(0);
        assertEquals(0, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] params = request.asHttpParameterArray();
        
        // Check if all parameters are included
        assertEquals(12, params.length);
        boolean idFound = false;
        boolean urlFound = false;
        boolean maxwidthFound = false;
        boolean hideMediaFound = false;
        boolean hideThreadFound = false;
        boolean omitScriptFound = false;
        boolean alignFound = false;
        boolean relatedFound = false;
        boolean langFound = false;
        
        for (HttpParameter param : params) {
            if (param.getName().equals("id")) {
                idFound = true;
                assertEquals(String.valueOf(request.statusId), param.getValue());
            } else if (param.getName().equals("url")) {
                urlFound = true;
                assertEquals(request.url, param.getValue());
            } else if (param.getName().equals("maxwidth")) {
                maxwidthFound = true;
                assertEquals(String.valueOf(request.maxWidth), param.getValue());
            } else if (param.getName().equals("hide_media")) {
                hideMediaFound = true;
                assertEquals(String.valueOf(request.hideMedia), param.getValue());
            } else if (param.getName().equals("hide_thread")) {
                hideThreadFound = true;
                assertEquals(String.valueOf(request.hideThread), param.getValue());
            } else if (param.getName().equals("omit_script")) {
                omitScriptFound = true;
                assertEquals(String.valueOf(request.omitScript), param.getValue());
            } else if (param.getName().equals("align")) {
                alignFound = true;
                assertEquals(request.align.name().toLowerCase(), param.getValue());
            } else if (param.getName().equals("related")) {
                relatedFound = true;
                assertEquals(StringUtil.join(request.related), param.getValue());
            } else if (param.getName().equals("lang")) {
                langFound = true;
                assertEquals(request.lang, param.getValue());
            }
        }
        
        assertTrue(idFound);
        assertTrue(urlFound);
        assertTrue(maxwidthFound);
        assertTrue(hideMediaFound);
        assertTrue(hideThreadFound);
        assertTrue(omitScriptFound);
        assertTrue(alignFound);
        assertTrue(relatedFound);
        assertTrue(langFound);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertTrue(request1.equals(request2));
        
        // Test inequality
        OEmbedRequest request3 = new OEmbedRequest(54321, "https://example.com");
        assertFalse(request1.equals(request3));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
        
        // Test inequality
        OEmbedRequest request3 = new OEmbedRequest(54321, "https://example.com");
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}",
                request1.toString());
    }
}