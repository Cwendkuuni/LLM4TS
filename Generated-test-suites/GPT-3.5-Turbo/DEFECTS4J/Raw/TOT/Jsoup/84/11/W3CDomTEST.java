import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoup_typicalCase() {
        // Create a jsoup Document with typical elements
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        
        // Convert the jsoup Document to W3C Document
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Verify that the conversion was successful by checking the root element
        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testFromJsoup_emptyDocument() {
        // Create an empty jsoup Document
        Document jsoupDoc = Jsoup.parse("");

        // Convert the jsoup Document to W3C Document
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Verify that the conversion was successful by checking the root element
        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertNull(rootElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullDocument() {
        // Convert a null jsoup Document to W3C Document
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_typicalCase() {
        // Create a jsoup Document with typical elements
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        
        // Create a W3C Document
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        
        // Convert the jsoup Document to the existing W3C Document
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Verify that the conversion was successful by checking the root element
        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert_emptyDocument() {
        // Create an empty jsoup Document
        Document jsoupDoc = Jsoup.parse("");

        // Create a W3C Document
        org.w3c.dom.Document w3cDoc = createW3CDocument();

        // Convert the jsoup Document to the existing W3C Document
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Verify that the conversion was successful by checking the root element
        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertNull(rootElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullDocument() {
        // Create a jsoup Document with typical elements
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        
        // Convert a null jsoup Document to an existing W3C Document
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, null);
    }

    @Test
    public void testAsString_typicalCase() {
        // Create a W3C Document with typical elements
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        
        // Create a W3CDom instance
        W3CDom w3cDom = new W3CDom();
        
        // Convert the W3C Document to a string
        String docString = w3cDom.asString(w3cDoc);
        
        // Verify that the converted string contains the expected elements
        assertTrue(docString.contains("<html>"));
        assertTrue(docString.contains("<body>"));
        assertTrue(docString.contains("<h1>Test</h1>"));
    }

    @Test
    public void testAsString_emptyDocument() {
        // Create an empty W3C Document
        org.w3c.dom.Document w3cDoc = createW3CDocument();

        // Create a W3CDom instance
        W3CDom w3cDom = new W3CDom();

        // Convert the empty W3C Document to a string
        String docString = w3cDom.asString(w3cDoc);

        // Verify that the converted string is empty
        assertEquals("", docString);
    }

    private org.w3c.dom.Document createW3CDocument() {
        // Create a new W3C Document
        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            fail("Failed to create W3C Document");
        }
        return w3cDoc;
    }
}