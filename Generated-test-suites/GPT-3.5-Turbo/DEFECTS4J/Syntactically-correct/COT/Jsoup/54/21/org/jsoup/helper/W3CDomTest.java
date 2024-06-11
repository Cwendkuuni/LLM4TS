import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup() {
        // Test case with a basic jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>");
        
        // Conversion from jsoup to W3C Document
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello", ((Element)w3cDoc.getElementsByTagName("h1").item(0)).getTextContent());
    }
    
    @Test
    public void testConvert() {
        // Test case with a basic jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>");
        
        // Conversion from jsoup to W3C Document
        Document w3cDoc = Jsoup.convert(jsoupDoc);
        
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello", ((Element)w3cDoc.getElementsByTagName("h1").item(0)).getTextContent());
    }
    
    @Test
    public void testAsString() {
        // Test case with a basic W3C Document
        Document w3cDoc = w3cDom.fromJsoup(Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>"));
        
        String result = w3cDom.asString(w3cDoc);
        
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("Hello"));
    }
}