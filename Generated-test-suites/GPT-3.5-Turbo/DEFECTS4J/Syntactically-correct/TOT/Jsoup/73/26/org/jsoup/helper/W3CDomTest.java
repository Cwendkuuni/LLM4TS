import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_withNullInputDocument_throwException() {
        try {
            w3cDom.fromJsoup(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input document cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFromJsoup_withValidInputDocument_returnValidW3CDocument() {
        org.jsoup.nodes.Document inputDoc = createValidJsoupDocument();

        Document outputDoc = w3cDom.fromJsoup(inputDoc);

        assertNotNull(outputDoc);
        assertEquals(inputDoc.location(), outputDoc.getDocumentURI());
    }

    @Test
    public void testConvert_withValidJsoupDocumentAndW3CDocument_convertSuccessfully() {
        org.jsoup.nodes.Document inputDoc = createValidJsoupDocument();

        Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            fail("Parsing configuration exception occurred: " + e.getMessage());
        }

        w3cDom.convert(inputDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
        assertEquals(inputDoc.location(), w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_withBlankLocationJsoupDocument_setDocumentURIAsBlank() {
        org.jsoup.nodes.Document inputDoc = createBlankLocationJsoupDocument();

        Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            fail("Parsing configuration exception occurred: " + e.getMessage());
        }

        w3cDom.convert(inputDoc, w3cDoc);

        assertNotNull(w3cDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_withValidJsoupDocumentAndW3CDocument_convertSuccessfullyWithAttributesAndTextNodes() {
        org.jsoup.nodes.Document inputDoc = createValidJsoupDocumentWithAttributesAndTextNodes();

        Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            fail("Parsing configuration exception occurred: " + e.getMessage());
        }

        w3cDom.convert(inputDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());

        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());

        Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
        assertNotNull(divElement);
        assertEquals("div", divElement.getTagName());

        Attr attribute = divElement.getAttributeNode("class");
        assertNotNull(attribute);
        assertEquals("my-class", attribute.getValue());

        Text textNode = (Text) divElement.getChildNodes().item(0);
        assertNotNull(textNode);
        assertEquals("This is a text node", textNode.getData());
    }

    @Test
    public void testAsString_withValidW3CDocument_returnValidStringRepresentation() {
        Document w3cDoc = createValidW3CDocument();

        String outputString = w3cDom.asString(w3cDoc);

        assertNotNull(outputString);
        assertTrue(outputString.contains("<html>"));
        assertTrue(outputString.contains("<body>"));
        assertTrue(outputString.contains("<div class=\"test\">"));
        assertTrue(outputString.contains("This is a test."));
        assertTrue(outputString.contains("</div>"));
        assertTrue(outputString.contains("</body>"));
        assertTrue(outputString.contains("</html>"));
    }

    private org.jsoup.nodes.Document createValidJsoupDocument() {
        org.jsoup.nodes.Document document = org.jsoup.Jsoup.parse("<html><body><div></div></body></html>");
        return document;
    }

    private org.jsoup.nodes.Document createBlankLocationJsoupDocument() {
        org.jsoup.nodes.Document document = org.jsoup.Jsoup.parse("<html><body><div></div></body></html>");
        document.location("");
        return document;
    }

    private org.jsoup.nodes.Document createValidJsoupDocumentWithAttributesAndTextNodes() {
        org.jsoup.nodes.Document document = org.jsoup.Jsoup.parse("<html><body><div class=\"my-class\">This is a text node</div></body></html>");
        return document;
    }

    private Document createValidW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            fail("Parsing configuration exception occurred: " + e.getMessage());
            return null;
        }

        org.w3c.dom.Document w3cDoc = builder.newDocument();
        Element htmlElement = w3cDoc.createElement("html");
        Element bodyElement = w3cDoc.createElement("body");
        Element divElement = w3cDoc.createElement("div");
        Attr classAttribute = w3cDoc.createAttribute("class");
        classAttribute.setValue("test");
        divElement.setAttributeNode(classAttribute);
        Text textNode = w3cDoc.createTextNode("This is a test.");
        divElement.appendChild(textNode);
        bodyElement.appendChild(divElement);
        htmlElement.appendChild(bodyElement);
        w3cDoc.appendChild(htmlElement);

        return w3cDoc;
    }
}