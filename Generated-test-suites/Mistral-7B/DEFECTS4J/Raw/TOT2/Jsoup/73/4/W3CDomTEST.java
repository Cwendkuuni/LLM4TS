import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        // When converting it to a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        // Then the W3C Document should not be null
        assertNotNull(w3cDoc);
        // And its root element should have the correct tag name
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        // Given a jsoup Document and a W3C Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        // When converting the jsoup Document to the W3C Document again
        Document convertedW3CDoc = new W3CDom().convert(jsoupDoc, w3cDoc);
        // Then the converted W3C Document should be equal to the original W3C Document
        assertEquals(w3cDoc, convertedW3CDoc);
    }

    @Test
    public void testAsString() {
        // Given a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>"));
        // When serializing it to a String
        String w3cString = new W3CDom().asString(w3cDoc);
        // Then the String should not be null
        assertNotNull(w3cString);
        // And its content should match the original W3C Document
        Document parsedW3CString = new DocumentBuilderFactory().newDocumentBuilder().parseNewDocument(new ByteArrayInputStream(w3cString.getBytes()));
        assertEquals(w3cDoc, parsedW3CString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        // Given a null jsoup Document
        new W3CDom().fromJsoup(null);
        // Then an IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullDocument() {
        // Given a null jsoup Document
        Document jsoupDoc = null;
        // And a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>"));
        // When trying to convert the jsoup Document to the W3C Document with a null jsoup Document
        new W3CDom().convert(jsoupDoc, w3cDoc);
        // Then an IllegalArgumentException should be thrown
    }
}