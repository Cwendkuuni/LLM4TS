
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.Jsoup;

import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        // Create a new jsoup document
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        // Create a new W3CDom instance
        W3CDom w3cDom = new W3CDom();

        // Convert the jsoup document to W3C document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Ensure the W3C document is not null
        assertNotNull(w3cDoc);

        // Ensure the W3C document has a root element
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);

        // Ensure the root element has the correct tag name
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        // Create a new jsoup document
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);

        // Create a new W3C document using the DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        // Create a new W3CDom instance
        W3CDom w3cDom = new W3CDom();

        // Convert the jsoup document to W3C document
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Ensure the W3C document is not null
        assertNotNull(w3cDoc);

        // Ensure the W3C document has a root element
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);

        // Ensure the root element has the correct tag name
        assertEquals("html", rootElement.getTagName());

        // Ensure the W3C document as string is not null
        String w3cDocString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cDocString);

        // Ensure the W3C document as string is the same as the original jsoup document as string
        assertEquals(jsoupDoc.html(), Parser.unescapeEntities(w3cDocString, true));
    }

    @Test
    public void testAsString() throws ParserConfigurationException {
        // Create a new W3C document using the DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        // Create an element and append it to the document
        Element element = w3cDoc.createElement("test");
        w3cDoc.appendChild(element);

        // Create a new W3CDom instance
        W3CDom w3cDom = new W3CDom();

        // Serialize the W3C document to string
        String w3cDocString = w3cDom.asString(w3cDoc);

        // Ensure the W3C document as string is not null
        assertNotNull(w3cDocString);

        // Ensure the W3C document as string is the same as the expected string
        assertEquals("<test></test>", w3cDocString);
    }
}
