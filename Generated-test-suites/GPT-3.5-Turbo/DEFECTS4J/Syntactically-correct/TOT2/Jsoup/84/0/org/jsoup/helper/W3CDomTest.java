import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        
        // Typical use case
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Edge case: null input
        try {
            w3cDom.fromJsoup(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Typical use case
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Edge case: empty jsoup document
        try {
            w3cDom.convert(new org.jsoup.nodes.Document(""), w3cDoc);
            fail("Expected IllegalStateException for empty jsoup document");
        } catch (IllegalStateException e) {
            // success
        }
    }

    @Test
    public void testW3CBuilder() {
        // This method is protected and can't be tested directly
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        
        // Typical use case
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        
        // Edge case: null input
        try {
            w3cDom.asString(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}