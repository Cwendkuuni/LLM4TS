import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {
    @Test
    public void testConstructor_defaultValues() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(12345, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
        assertEquals(true, request.isHideMedia());
        assertEquals(true, request.isHideThread());
        assertEquals(false, request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }
    
    @Test
    public void testSetMaxWidth_validValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.getMaxWidth());
    }
    
    @Test
    public void testSetMaxWidth_invalidValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(-1);
        assertEquals(0, request.getMaxWidth());
    }
    
    @Test
    public void testSetHideMedia_true() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(true);
        assertEquals(true, request.isHideMedia());
    }
    
    @Test
    public void testSetHideMedia_false() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assertEquals(false, request.isHideMedia());
    }
    
    @Test
    public void testSetHideThread_true() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(true);
        assertEquals(true, request.isHideThread());
    }
    
    @Test
    public void testSetHideThread_false() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assertEquals(false, request.isHideThread());
    }
    
    @Test
    public void testSetOmitScript_true() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assertEquals(true, request.isOmitScript());
    }
    
    @Test
    public void testSetOmitScript_false() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(false);
        assertEquals(false, request.isOmitScript());
    }
    
    @Test
    public void testSetAlign_validValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());
    }
    
    @Test
    public void testSetRelated_validValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.getRelated());
    }
    
    @Test
    public void testSetLang_validValue() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assertEquals("en", request.getLang());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        request.setHideThread(false);
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        HttpParameter[] params = request.asHttpParameterArray();
        assertEquals(8, params.length);
        assertEquals("id", params[0].getName());
        assertEquals("12345", params[0].getValue());
        assertEquals("url", params[1].getName());
        assertEquals("https://example.com", params[1].getValue());
        assertEquals("maxwidth", params[2].getName());
        assertEquals("500", params[2].getValue());
        assertEquals("hide_media", params[3].getName());
        assertEquals("true", params[3].getValue());
        assertEquals("hide_thread", params[4].getName());
        assertEquals("false", params[4].getValue());
        assertEquals("omit_script", params[5].getName());
        assertEquals("false", params[5].getValue());
        assertEquals("align", params[6].getName());
        assertEquals("none", params[6].getValue());
        assertEquals("related", params[7].getName());
        assertEquals("user1,user2", params[7].getValue());
    }
    
    @Test
    public void testEquals_sameInstance() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(true, request.equals(request));
    }
    
    @Test
    public void testEquals_differentClass() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        Object obj = new Object();
        assertEquals(false, request.equals(obj));
    }
    
    @Test
    public void testEquals_differentHideMedia() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setHideMedia(false);
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentHideThread() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setHideThread(false);
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentMaxWidth() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setMaxWidth(500);
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentOmitScript() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setOmitScript(true);
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentStatusId() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(54321, "https://example.com");
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentAlign() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setAlign(Align.LEFT);
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentLang() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setLang("en");
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_differentRelated() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        request2.setRelated(related);
        assertEquals(false, request1.equals(request2));
    }
    
    @Test
    public void testEquals_sameValues() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(true, request1.equals(request2));
    }
    
    @Test
    public void testEquals_nullUrl() {
        OEmbedRequest request1 = new OEmbedRequest(12345, null);
        OEmbedRequest request2 = new OEmbedRequest(12345, null);
        assertEquals(true, request1.equals(request2));
    }
    
    @Test
    public void testEquals_nullLang() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        request1.setLang(null);
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        request2.setLang(null);
        assertEquals(true, request1.equals(request2));
    }
    
    @Test
    public void testHashCode_sameInstance() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request.hashCode(), request.hashCode());
    }
    
    @Test
    public void testHashCode_sameValues() {
        OEmbedRequest request1 = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345, "https://example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        assertEquals("OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=none, related=[], lang='null'}", request.toString());
    }
}