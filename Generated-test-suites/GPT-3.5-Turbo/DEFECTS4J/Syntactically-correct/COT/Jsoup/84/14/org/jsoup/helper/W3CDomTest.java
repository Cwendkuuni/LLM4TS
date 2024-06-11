
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        // Test normal conversion
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Test null input
        try {
            w3cDom.fromJsoup(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testConvert() {
        // Test normal conversion
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc, convertedDoc);

        // Test blank location
        jsoupDoc.location(" ");
        convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc, convertedDoc);

        // Test non-blank location
        jsoupDoc.location("http://example.com");
        convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc, convertedDoc);

        // Test null input
        try {
            w3cDom.convert(null, w3cDoc);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test null output
        try {
            w3cDom.convert(jsoupDoc, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testAsString() {
        // Test normal conversion
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cStr = w3cDom.asString(w3cDoc);
        assertNotNull(w3cStr);
        assertTrue(w3cStr.length() > 0);

        // Test null input
        try {
            w3cDom.asString(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
