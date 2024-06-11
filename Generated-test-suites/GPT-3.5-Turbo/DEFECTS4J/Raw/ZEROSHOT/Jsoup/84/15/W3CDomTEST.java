import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {
    @Test
    public void testFromJsoup() throws ParserConfigurationException, IOException {
        // Create a jsoup document
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        Document jsoupDoc = Parser.parse(html, "");

        // Convert jsoup document to W3C document
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert the W3C document contains the expected elements
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("head", w3cDoc.getDocumentElement().getElementsByTagName("head").item(0).getNodeName());
        assertEquals("title", w3cDoc.getDocumentElement().getElementsByTagName("title").item(0).getNodeName());
        assertEquals("body", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("h1", w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0).getNodeName());
    }

    @Test
    public void testConvert() throws ParserConfigurationException, IOException {
        // Create a jsoup document
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        Document jsoupDoc = Parser.parse(html, "");

        // Convert jsoup document to W3C document
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert the W3C document contains the expected elements
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("head", w3cDoc.getDocumentElement().getElementsByTagName("head").item(0).getNodeName());
        assertEquals("title", w3cDoc.getDocumentElement().getElementsByTagName("title").item(0).getNodeName());
        assertEquals("body", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("h1", w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0).getNodeName());
    }

    @Test
    public void testW3CBuilder() throws ParserConfigurationException, IOException {
        // Create a jsoup document
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        Document jsoupDoc = Parser.parse(html, "");

        // Convert jsoup document to W3C document using custom W3CBuilder
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        NodeTraversor.traverse(new W3CDom.W3CBuilder(w3cDoc), jsoupDoc);
        w3cBuilder.convert(jsoupDoc, w3cDoc);

        // Assert the W3C document contains the expected elements
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("head", w3cDoc.getDocumentElement().getElementsByTagName("head").item(0).getNodeName());
        assertEquals("title", w3cDoc.getDocumentElement().getElementsByTagName("title").item(0).getNodeName());
        assertEquals("body", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("h1", w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0).getNodeName());
    }

    @Test
    public void testCopyAttributes() throws ParserConfigurationException {
        // Create a jsoup element with attributes
        Element jsoupEl = new Element("div");
        jsoupEl.attr("id", "test");
        jsoupEl.attr("class", "example");

        // Create a W3C Document and Element
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        org.w3c.dom.Element w3cEl = w3cDoc.createElement("div");

        // Copy attributes from jsoup element to W3C element
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.copyAttributes(jsoupEl, w3cEl);

        // Assert the W3C element contains the expected attributes
        assertEquals("test", w3cEl.getAttribute("id"));
        assertEquals("example", w3cEl.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() throws ParserConfigurationException {
        // Create a jsoup element with namespaces
        Element jsoupEl = new Element("div", "http://www.example.com");
        jsoupEl.attr("xmlns", "http://www.example.com");
        jsoupEl.attr("xmlns:prefix", "http://www.example.com/prefix");
        jsoupEl.attr("xmlns:ns1", "http://www.example.com/ns1");
        jsoupEl.attr("prefix:attr1", "value1");
        jsoupEl.attr("ns1:attr2", "value2");

        // Create a W3C Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        // Update namespaces in W3C document
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = w3cBuilder.updateNamespaces(jsoupEl);

        // Assert the W3C document contains the expected namespaces and returns the correct prefix
        assertEquals("http://www.example.com", w3cBuilder.namespacesStack.peek().get(""));
        assertEquals("http://www.example.com/prefix", w3cBuilder.namespacesStack.peek().get("prefix"));
        assertEquals("prefix", prefix);
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        // Create a W3C document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        org.w3c.dom.Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);

        // Serialize W3C document to string
        W3CDom w3cDom = new W3CDom();
        String xml = w3cDom.asString(w3cDoc);

        // Assert the serialized xml
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>", xml);
    }
}