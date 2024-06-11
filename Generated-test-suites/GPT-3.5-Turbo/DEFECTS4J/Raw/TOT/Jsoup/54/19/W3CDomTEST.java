import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        
        // Check if the converted document contains a title element
        assertNotNull(w3cDoc.getElementsByTagName("title"));
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        
        // Check if the converted document contains a title element
        assertNotNull(w3cDoc.getElementsByTagName("title"));
        
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Check if the converted document contains the elements from the original jsoup document
        assertNotNull(w3cDoc.getElementsByTagName("html"));
        assertNotNull(w3cDoc.getElementsByTagName("head"));
        assertNotNull(w3cDoc.getElementsByTagName("body"));
        assertNotNull(w3cDoc.getElementsByTagName("h1"));
        
        // Check if the converted document has the correct text content
        assertEquals("Test Document", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }
    
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String w3cDocString = w3cDom.asString(w3cDoc);
        
        assertNotNull(w3cDocString);
        assertFalse(w3cDocString.isEmpty());
        
        // Check if the converted string contains the correct HTML tags and content
        assertTrue(w3cDocString.contains("<html>"));
        assertTrue(w3cDocString.contains("<head>"));
        assertTrue(w3cDocString.contains("<title>Test Document</title>"));
        assertTrue(w3cDocString.contains("<body>"));
        assertTrue(w3cDocString.contains("<h1>Hello World!</h1>"));
    }
}