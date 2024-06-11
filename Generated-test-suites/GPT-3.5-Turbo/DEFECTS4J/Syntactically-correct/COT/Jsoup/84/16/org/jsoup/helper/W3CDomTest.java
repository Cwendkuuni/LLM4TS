import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public class W3CDomTest {
    
    // Test for the method fromJsoup(org.jsoup.nodes.Document)
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = parseHtml("<html><body><h1>Hello World</h1></body></html>");
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);
        // Assert the converted document is not null
        assertNotNull(w3cDoc);
        // Assert the document contains the expected elements
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
        Element bodyElement = rootElement.getChildNodes().item(0);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());
        Element h1Element = bodyElement.getChildNodes().item(0);
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.getTagName());
        TextNode textNode = (TextNode) h1Element.getChildNodes().item(0);
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.getWholeText());
    }
    
    // Test for the method convert(org.jsoup.nodes.Document, Document)
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = parseHtml("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = createEmptyW3CDocument();
        W3CDom converter = new W3CDom() {
            @Override
            protected DocumentBuilderFactory getFactory() {
                // Mock the DocumentBuilderFactory for testing purposes
                return new MockDocumentBuilderFactory();
            }
        };
        converter.convert(jsoupDoc, w3cDoc);
        // Assert the converted document is not null
        assertNotNull(w3cDoc);
        // Assert the document contains the expected elements
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
        Element bodyElement = rootElement.getChildNodes().item(0);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());
        Element h1Element = bodyElement.getChildNodes().item(0);
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.getTagName());
        Text textNode = (Text) h1Element.getChildNodes().item(0);
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.getWholeText());
    }
    
    // Test for the method copyAttributes(Node, Element)
    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = parseHtml("<div id=\"myDiv\" class=\"container\"></div>").body().child(0);
        Element w3cElement = createEmptyW3CElement();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(createEmptyW3CDocument());
        builder.copyAttributes(jsoupElement, w3cElement);
        // Assert the copied attributes are correct
        assertNotNull(w3cElement.getAttribute("id"));
        assertEquals("myDiv", w3cElement.getAttribute("id"));
        assertNotNull(w3cElement.getAttribute("class"));
        assertEquals("container", w3cElement.getAttribute("class"));
    }
    
    // Test for the method updateNamespaces(org.jsoup.nodes.Element)
    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = parseHtml("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:jsoup=\"http://www.jsoup.org\"></html>").body().child(0);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(createEmptyW3CDocument());
        String prefix = builder.updateNamespaces(jsoupElement);
        // Assert the updated namespaces are correct
        assertNotNull(prefix);
        assertEquals("jsoup", prefix);
        assertNotNull(builder.namespacesStack.peek().get("jsoup"));
        assertEquals("http://www.jsoup.org", builder.namespacesStack.peek().get("jsoup"));
    }
    
    // Test for the method asString(Document)
    @Test
    public void testAsString() {
        Document w3cDoc = createEmptyW3CDocument();
        W3CDom converter = new W3CDom();
        String xmlString = converter.asString(w3cDoc);
        // Assert the serialized string is not null and not empty
        assertNotNull(xmlString);
        assertFalse(xmlString.isEmpty());
        // Assert the serialized string is valid XML
        assertValidXml(xmlString);
    }
    
    // Helper method to parse HTML using Jsoup
    private org.jsoup.nodes.Document parseHtml(String html) {
        return Parser.parse(html, "");
    }
    
    // Helper method to create an empty W3C Document
    private Document createEmptyW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
    
    // Helper method to create an empty W3C Element
    private Element createEmptyW3CElement() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc.createElement("test");
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
    
    // Helper method to assert that a string is valid XML
    private void assertValidXml(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            fail("Invalid XML: " + e.getMessage());
        }
    }
}