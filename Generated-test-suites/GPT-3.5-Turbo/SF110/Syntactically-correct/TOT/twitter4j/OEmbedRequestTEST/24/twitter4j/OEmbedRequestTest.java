import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructorAndGetters() {
        long statusId = 123456789;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertEquals(true, request.isHideMedia());
        assertEquals(true, request.isHideThread());
        assertEquals(false, request.isOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }
    
    @Test
    public void testSetMaxWidthAndGetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        int maxWidth = 800;
        request.setMaxWidth(maxWidth);
        
        assertEquals(maxWidth, request.getMaxWidth());
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        int maxWidth = 800;
        OEmbedRequest result = request.maxWidth(maxWidth);
        
        assertEquals(maxWidth, result.getMaxWidth());
        assertSame(request, result);
    }
    
    @Test
    public void testSetHideMediaAndIsHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        boolean hideMedia = true;
        request.setHideMedia(hideMedia);
        
        assertEquals(hideMedia, request.isHideMedia());
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        boolean hideMedia = true;
        OEmbedRequest result = request.HideMedia(hideMedia);
        
        assertEquals(hideMedia, result.isHideMedia());
        assertSame(request, result);
    }
    
    @Test
    public void testSetHideThreadAndIsHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        boolean hideThread = true;
        request.setHideThread(hideThread);
        
        assertEquals(hideThread, request.isHideThread());
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        boolean hideThread = true;
        OEmbedRequest result = request.HideThread(hideThread);
        
        assertEquals(hideThread, result.isHideThread());
        assertSame(request, result);
    }
    
    @Test
    public void testSetOmitScriptAndIsOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        boolean omitScript = true;
        request.setOmitScript(omitScript);
        
        assertEquals(omitScript, request.isOmitScript());
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        boolean omitScript = true;
        OEmbedRequest result = request.omitScript(omitScript);
        
        assertEquals(omitScript, result.isOmitScript());
        assertSame(request, result);
    }
    
    @Test
    public void testSetAlignAndGetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        request.setAlign(align);
        
        assertEquals(align, request.getAlign());
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        OEmbedRequest result = request.align(align);
        
        assertEquals(align, result.getAlign());
        assertSame(request, result);
    }
    
    @Test
    public void testSetRelatedAndGetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        
        assertArrayEquals(related, request.getRelated());
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        String[] related = {"user1", "user2"};
        OEmbedRequest result = request.related(related);
        
        assertArrayEquals(related, result.getRelated());
        assertSame(request, result);
    }
    
    @Test
    public void testSetLangAndgetLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        String lang = "en";
        request.setLang(lang);
        
        assertEquals(lang, request.getLang());
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        
        String lang = "en";
        OEmbedRequest result = request.lang(lang);
        
        assertEquals(lang, result.getLang());
        assertSame(request, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(800);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[] {"user1", "user2"});
        request.setLang("en");
        
        HttpParameter[] expectedParams = {new HttpParameter("id", "123456789"), new HttpParameter("url", "https://example.com"),
                                          new HttpParameter("maxwidth", 800), new HttpParameter("hide_media", true),
                                          new HttpParameter("hide_thread", false), new HttpParameter("omit_script", true),
                                          new HttpParameter("align", "center"), new HttpParameter("related", "user1,user2"),
                                          new HttpParameter("lang", "en")};
        HttpParameter[] actualParams = request.asHttpParameterArray();
        
        assertArrayEquals(expectedParams, actualParams);
    }
    
    @Test
    public void testEquals() {
        long statusId = 123456789;
        String url = "https://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId, url);
        OEmbedRequest request2 = new OEmbedRequest(statusId, url);
        OEmbedRequest request3 = new OEmbedRequest(987654321, url);
        OEmbedRequest request4 = new OEmbedRequest(statusId, "https://differenturl.com");
        
        assertEquals(true, request1.equals(request2));
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.equals(request4));
    }
    
    @Test
    public void testHashCode() {
        long statusId = 123456789;
        String url = "https://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId, url);
        OEmbedRequest request2 = new OEmbedRequest(statusId, url);
        
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString() {
        long statusId = 123456789;
        String url = "https://example.com";
        int maxWidth = 800;
        boolean hideMedia = true;
        boolean hideThread = false;
        boolean omitScript = true;
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        String[] related = {"user1", "user2"};
        String lang = "en";
        
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);
        
        String expectedString = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=800, "
                + "hideMedia=true, hideThread=false, omitScript=true, align=center, related=[user1, user2], lang='en'}";
        String actualString = request.toString();
        
        assertEquals(expectedString, actualString);
    }
}