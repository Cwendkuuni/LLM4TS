import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        assertEquals(12345L, request.statusId);
        assertEquals("https://www.example.com", request.url);
        assertEquals(true, request.hideMedia);
        assertEquals(true, request.hideThread);
        assertEquals(false, request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }
    
    @Test
    public void testSetMaxWidth(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testMaxWidth(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.MaxWidth(500);
        assertEquals(request, result);
        assertEquals(500, request.maxWidth);
    }
    
    @Test
    public void testSetHideMedia(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setHideMedia(false);
        assertEquals(false, request.hideMedia);
    }
    
    @Test
    public void testHideMedia(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.HideMedia(false);
        assertEquals(request, result);
        assertEquals(false, request.hideMedia);
    }
    
    @Test
    public void testSetHideThread(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setHideThread(false);
        assertEquals(false, request.hideThread);
    }
    
    @Test
    public void testHideThread(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.HideThread(false);
        assertEquals(request, result);
        assertEquals(false, request.hideThread);
    }
    
    @Test
    public void testSetOmitScript(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setOmitScript(true);
        assertEquals(true, request.omitScript);
    }
    
    @Test
    public void testOmitScript(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.omitScript(true);
        assertEquals(request, result);
        assertEquals(true, request.omitScript);
    }
    
    @Test
    public void testSetAlign(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    @Test
    public void testAlign(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.align(OEmbedRequest.Align.LEFT);
        assertEquals(request, result);
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }
    
    @Test
    public void testSetRelated(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testRelated(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        String[] related = new String[]{"related1", "related2"};
        OEmbedRequest result = request.related(related);
        assertEquals(request, result);
        assertArrayEquals(related, request.related);
    }
    
    @Test
    public void testSetLang(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setLang("en");
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testLang(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest result = request.lang("en");
        assertEquals(request, result);
        assertEquals("en", request.lang);
    }
    
    @Test
    public void testAsHttpParameterArray(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        
        HttpParameter[] expectedParams = new HttpParameter[]{
            new HttpParameter("id", "12345"),
            new HttpParameter("url", "https://www.example.com"),
            new HttpParameter("maxwidth", 500),
            new HttpParameter("hide_media", false),
            new HttpParameter("hide_thread", false),
            new HttpParameter("omit_script", true),
            new HttpParameter("align", "left"),
            new HttpParameter("related", "related1,related2"),
            new HttpParameter("lang", "en")
        };
        
        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }
    
    @Test
    public void testEquals(){
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://www.example.com");
        assertEquals(true, request1.equals(request2));
    }
    
    @Test
    public void testHashCode(){
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://www.example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://www.example.com");
        assertEquals(request1.hashCode(), request2.hashCode());
    }
    
    @Test
    public void testToString(){
        OEmbedRequest request = new OEmbedRequest(12345L, "https://www.example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.setLang("en");
        
        String expectedString = "OEmbedRequest{statusId=12345, url='https://www.example.com', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=LEFT, related=[related1, related2], lang='en'}";
        
        assertEquals(expectedString, request.toString());
    }
}