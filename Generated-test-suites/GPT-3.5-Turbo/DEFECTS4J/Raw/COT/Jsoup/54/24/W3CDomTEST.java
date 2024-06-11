import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Test 1: Creating a valid jsoup document object
        String html = "<html><body><h1>Test</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }
    
    @Test
    public void testConvert() {
        // Test 2: Converting a jsoup document to a W3C document
        String html = "<html><body><h1>Test</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        // Test 3: Converting a W3C document to a string
        String html = "<html><body><h1>Test</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String w3cString = w3cDom.asString(w3cDoc);

        assertNotNull(w3cString);
        assertTrue(w3cString.contains("<h1>Test</h1>"));
    }
}