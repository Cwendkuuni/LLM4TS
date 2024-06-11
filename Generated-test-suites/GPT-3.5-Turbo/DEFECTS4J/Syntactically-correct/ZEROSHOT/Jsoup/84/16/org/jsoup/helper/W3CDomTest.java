import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        // Test null input
        W3CDom w3cDom = new W3CDom();
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(null);
        });

        // Test valid input
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            Document convertedDoc = w3cDom.fromJsoup(jsoupDoc);
            assertEquals(w3cDoc, convertedDoc);
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }
    }

    @Test
    public void testConvert() {
        // Test blank location
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertEquals("", w3cDoc.getDocumentURI());
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }

        // Test non-blank location
        jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertEquals("http://example.com", w3cDoc.getDocumentURI());
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }
    }

    @Test
    public void testW3CBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

            // Test head() method
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
            org.jsoup.nodes.Attribute attribute = new org.jsoup.nodes.Attribute("class", "test");
            sourceEl.attributes().put(attribute);
            org.jsoup.nodes.Node sourceNode = sourceEl;
            w3cBuilder.head(sourceNode, 0);
            Element dest = w3cDoc.getDocumentElement();
            assertEquals("", dest.getTagName());
            assertEquals("test", dest.getAttribute("class"));

            // Test tail() method
            w3cBuilder.tail(sourceNode, 0);
            assertNull(w3cBuilder.dest);

            // Test copyAttributes() method
            org.jsoup.nodes.Node sourceNode2 = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("span"), "");
            org.jsoup.nodes.Attribute attribute2 = new org.jsoup.nodes.Attribute("id", "test");
            sourceEl.attributes().put(attribute2);
            Element el = w3cDoc.createElement("test");
            w3cBuilder.copyAttributes(sourceNode2, el);
            assertEquals("", el.getAttribute("class"));
            assertEquals("test", el.getAttribute("id"));

            // Test updateNamespaces() method
            org.jsoup.nodes.Element el2 = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("p"), "");
            org.jsoup.nodes.Attribute xmlnsAttr = new org.jsoup.nodes.Attribute("xmlns", "urn:test");
            el2.attributes().put(xmlnsAttr);
            String prefix = w3cBuilder.updateNamespaces(el2);
            assertEquals("", prefix);
            assertEquals("urn:test", w3cBuilder.namespacesStack.peek().get(""));

        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
            String output = w3cDom.asString(w3cDoc);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html><html></html>", output);
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }
    }
}