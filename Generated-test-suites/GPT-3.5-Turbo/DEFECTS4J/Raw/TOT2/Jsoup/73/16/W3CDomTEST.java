import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
      
        // Test conversion functionality
        assertTrue(w3cDoc instanceof Document);
        
        // Test for NullPointerException
        try {
            w3cDom.fromJsoup(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
      
        // Test conversion functionality
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
      
        // Test for rootElement
        assertEquals("root", rootElement.getTagName());
        
        // Test for blank location
        jsoupDoc.location("http://example.com");
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
      
        // Test serialization functionality
        String result = w3cDom.asString(w3cDoc);
        assertTrue(result.length() > 0);
        
        // Test for IllegalStateException
        try {
            w3cDom.asString(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

}