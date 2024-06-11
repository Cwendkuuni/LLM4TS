import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setMaxWidth(500);
        
        int expected = 500;
        int actual = request.getMaxWidth();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest updatedRequest = request.MaxWidth(500);
        
        int expected = 500;
        int actual = updatedRequest.getMaxWidth();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideMedia(false);
        
        boolean expected = false;
        boolean actual = request.isHideMedia();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest updatedRequest = request.HideMedia(false);
        
        boolean expected = false;
        boolean actual = updatedRequest.isHideMedia();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setHideThread(false);
        
        boolean expected = false;
        boolean actual = request.isHideThread();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest updatedRequest = request.HideThread(false);
        
        boolean expected = false;
        boolean actual = updatedRequest.isHideThread();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setOmitScript(true);
        
        boolean expected = true;
        boolean actual = request.isOmitScript();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest updatedRequest = request.omitScript(true);
        
        boolean expected = true;
        boolean actual = updatedRequest.isOmitScript();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setAlign(OEmbedRequest.Align.CENTER);
        
        OEmbedRequest.Align expected = OEmbedRequest.Align.CENTER;
        OEmbedRequest.Align actual = request.getAlign();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest updatedRequest = request.align(OEmbedRequest.Align.CENTER);
        
        OEmbedRequest.Align expected = OEmbedRequest.Align.CENTER;
        OEmbedRequest.Align actual = updatedRequest.getAlign();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"related1", "related2"};
        request.setRelated(related);
        
        String[] expected = {"related1", "related2"};
        String[] actual = request.getRelated();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        String[] related = {"related1", "related2"};
        OEmbedRequest updatedRequest = request.related(related);
        
        String[] expected = {"related1", "related2"};
        String[] actual = updatedRequest.getRelated();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        request.setLang("en");
        
        String expected = "en";
        String actual = request.getLang();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest updatedRequest = request.lang("en");
        
        String expected = "en";
        String actual = updatedRequest.getLang();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(123456, "https://example.com");
        
        boolean expected = true;
        boolean actual = request1.equals(request2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        int expected = -714591771;
        int actual = request.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456, "https://example.com");
        
        String expected = "OEmbedRequest{statusId=123456, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        String actual = request.toString();
        assertEquals(expected, actual);
    }
}