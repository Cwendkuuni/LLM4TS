import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() {
        // Typical use case
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Edge case: empty document
        jsoupDoc = Jsoup.parse("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Error scenario: null input
        try {
            w3cDom.fromJsoup(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }
    
    @Test
    public void testConvert() {
        // Typical use case
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Edge case: empty document
        jsoupDoc = Jsoup.parse("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Error scenario: null input
        try {
            w3cDom.convert(null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }
    
    @Test
    public void testAsString() {
        // Typical use case
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
        
        // Edge case: empty document
        jsoupDoc = Jsoup.parse("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
        
        // Error scenario: null input
        try {
            w3cDom.asString(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }

}