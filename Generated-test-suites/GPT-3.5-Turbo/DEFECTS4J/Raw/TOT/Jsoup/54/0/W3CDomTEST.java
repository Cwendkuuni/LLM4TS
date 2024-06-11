import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup_typicalCase() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }
    
    @Test
    public void testFromJsoup_missingJsoupDoc() {
        W3CDom w3cDom = new W3CDom();
        try {
            Document w3cDoc = w3cDom.fromJsoup(null);
            fail("Expected IllegalArgumentException but got no exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("The input jsoup document cannot be null.", e.getMessage());
        }
    }
    
    @Test
    public void testConvert_typicalCase() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1><p>Content</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = Jsoup.parse(html);
        
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
        Elements childElements = rootElement.children();
        assertEquals(2, childElements.size());
        assertEquals("head", childElements.get(0).getTagName());
        assertEquals("body", childElements.get(1).getTagName());
    }
    
    @Test
    public void testConvert_missingJsoupDoc() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1><p>Content</p></body></html>";
        Document w3cDoc = Jsoup.parse(html);
        
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.convert(null, w3cDoc);
            fail("Expected IllegalArgumentException but got no exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("The input jsoup document cannot be null.", e.getMessage());
        }
    }
    
    @Test
    public void testConvert_missingW3CDoc() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1><p>Content</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        
        W3CDom w3cDom = new W3CDom();
        try {
            Document w3cDoc = null;
            w3cDom.convert(jsoupDoc, w3cDoc);
            fail("Expected IllegalArgumentException but got no exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("The output W3C document cannot be null.", e.getMessage());
        }
    }
    
    @Test
    public void testAsString_typicalCase() {
        String html = "<html><head><title>Test</title></head><body><h1>Title</h1><p>Content</p></body></html>";
        Document w3cDoc = Jsoup.parse(html);
        
        W3CDom w3cDom = new W3CDom();
        String w3cString = w3cDom.asString(w3cDoc);
        
        assertNotNull(w3cString);
        assertTrue(w3cString.contains("<html>") && w3cString.contains("</html>"));
    }
    
    @Test
    public void testAsString_missingW3CDoc() {
        W3CDom w3cDom = new W3CDom();
        try {
            String w3cString = w3cDom.asString(null);
            fail("Expected IllegalArgumentException but got no exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("The input W3C document cannot be null.", e.getMessage());
        }
    }
}