
import org.junit.Test;

public class OEmbedRequestTest {
    
    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(500);
        assert request.maxWidth == 500;
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.MaxWidth(500);
        assert result.maxWidth == 500;
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideMedia(true);
        assert request.hideMedia;
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.HideMedia(true);
        assert result.hideMedia;
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setHideThread(true);
        assert request.hideThread;
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.HideThread(true);
        assert result.hideThread;
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setOmitScript(true);
        assert request.omitScript;
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.omitScript(true);
        assert result.omitScript;
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setAlign(Align.CENTER);
        assert request.align == Align.CENTER;
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.align(Align.CENTER);
        assert result.align == Align.CENTER;
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"twitterapi", "twitter"};
        request.setRelated(related);
        assert Arrays.equals(request.related, related);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        String[] related = {"twitterapi", "twitter"};
        OEmbedRequest result = request.related(related);
        assert Arrays.equals(result.related, related);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setLang("en");
        assert request.lang.equals("en");
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        OEmbedRequest result = request.lang("en");
        assert result.lang.equals("en");
    }
    
    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(Align.LEFT);
        request.setRelated(new String[]{"twitterapi", "twitter"});
        request.setLang("en");
        
        HttpParameter[] parameters = request.asHttpParameterArray();
        
        assert parameters.length == 9;
        assert parameters[0].getName().equals("id") && parameters[0].getValue().equals("123");
        assert parameters[1].getName().equals("url") && parameters[1].getValue().equals("https://example.com");
        assert parameters[2].getName().equals("maxwidth") && parameters[2].getValue().equals("500");
        assert parameters[3].getName().equals("hide_media") && parameters[3].getValue().equals(false);
        assert parameters[4].getName().equals("hide_thread") && parameters[4].getValue().equals(false);
        assert parameters[5].getName().equals("omit_script") && parameters[5].getValue().equals(true);
        assert parameters[6].getName().equals("align") && parameters[6].getValue().equals("left");
        assert parameters[7].getName().equals("related") && parameters[7].getValue().equals("twitterapi,twitter");
        assert parameters[8].getName().equals("lang") && parameters[8].getValue().equals("en");
    }
    
    // additional test cases for edge cases and exception handling scenarios can be added here
    
}
