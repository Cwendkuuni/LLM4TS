import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Typical use case
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        // Edge case - null input
        try {
            w3cDom.fromJsoup(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testConvert() {
        // Typical use case
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Edge case - empty location
        assertEquals("", w3cDoc.getDocumentURI());

        // Edge case - empty input document
        jsoupDoc = Jsoup.parse("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
    }

    @Test
    public void testAsString() {
        // Typical use case
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);

        // Edge case - null input
        try {
            w3cDom.asString(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
}