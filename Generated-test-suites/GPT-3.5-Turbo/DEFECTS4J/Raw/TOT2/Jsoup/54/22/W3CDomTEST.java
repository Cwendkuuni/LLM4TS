import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        
        // Typical use case
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Edge case: empty document
        org.jsoup.nodes.Document emptyDoc = new org.jsoup.nodes.Document("");
        Document emptyW3cDoc = w3cDom.fromJsoup(emptyDoc);
        assertNotNull(emptyW3cDoc);
        
        // Error scenario: null input
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(null);
        });
    }
    
    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        
        // Typical use case
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Edge case: document with multiple elements
        org.jsoup.nodes.Document multiElementDoc = new org.jsoup.nodes.Document("<html><head></head><body></body></html>");
        Document multiElementW3cDoc = w3cDom.fromJsoup(multiElementDoc);
        w3cDom.convert(multiElementDoc, multiElementW3cDoc);
        
        // Error scenario: null input document
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.convert(null, null);
        });
    }
    
    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        
        // Typical use case
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Edge case: empty document
        String emptyString = w3cDom.asString(null);
        assertNotNull(emptyString);
        
        // Error scenario: null input
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.asString(null);
        });
    }
}