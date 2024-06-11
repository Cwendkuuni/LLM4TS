import static org.junit.Assert.*;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
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

public class W3CDomTest {
    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        // Test when input jsoup document is not null
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        Document result = w3CDom.fromJsoup(in);
        assertNotNull(result);

        // Test when input jsoup document is null
        try {
            w3CDom.fromJsoup(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("The input jsoup document cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConvert() {
        // Test when input jsoup document and output W3C document are not null
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        Document out = w3CDom.fromJsoup(in);
        w3CDom.convert(in, out);
        assertNotNull(out.getDocumentURI());

        // Test when input jsoup document is null
        try {
            Document out2 = w3CDom.fromJsoup(null);
            w3CDom.convert(null, out2);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("The input jsoup document cannot be null", e.getMessage());
        }

        // Test when output W3C document is null
        try {
            org.jsoup.nodes.Document in2 = new org.jsoup.nodes.Document("");
            w3CDom.convert(in2, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("The output W3C document cannot be null", e.getMessage());
        }
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);

        // Test when source node is a jsoup element
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        w3CBuilder.head(source, 0);

        // Test when source node is a jsoup text node
        org.jsoup.nodes.TextNode source2 = new org.jsoup.nodes.TextNode("text", "");
        w3CBuilder.head(source2, 0);

        // Test when source node is a jsoup comment
        org.jsoup.nodes.Comment source3 = new org.jsoup.nodes.Comment("comment", "");
        w3CBuilder.head(source3, 0);

        // Test when source node is a jsoup data node
        org.jsoup.nodes.DataNode source4 = new org.jsoup.nodes.DataNode("data", "");
        w3CBuilder.head(source4, 0);
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);

        // Test when source node is a jsoup element
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        w3CBuilder.tail(source, 0);

        // Test when source node is not a jsoup element
        org.jsoup.nodes.TextNode source2 = new org.jsoup.nodes.TextNode("text", "");
        w3CBuilder.tail(source2, 0);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);

        // Test when the attribute key is a valid xml attribute name
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        source.attr("key", "value");
        Element el = w3CBuilder.doc.createElementNS("", "");
        w3CBuilder.copyAttributes(source, el);
        assertTrue(el.hasAttribute("key"));

        // Test when the attribute key is not a valid xml attribute name
        org.jsoup.nodes.Element source2 = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        source2.attr("key!", "value");
        Element el2 = w3CBuilder.doc.createElementNS("", "");
        w3CBuilder.copyAttributes(source2, el2);
        assertFalse(el2.hasAttribute("key!"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);

        // Test when the element contains xmlns attribute
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        el.attr("xmlns", "namespace");
        String prefix = w3CBuilder.updateNamespaces(el);
        assertEquals("", prefix);
        assertTrue(w3CBuilder.namespaces.containsKey(""));
        assertEquals("namespace", w3CBuilder.namespaces.get(""));

        // Test when the element contains xmlns:prefix attribute
        org.jsoup.nodes.Element el2 = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        el2.attr("xmlns:prefix", "namespace");
        String prefix2 = w3CBuilder.updateNamespaces(el2);
        assertEquals("prefix", prefix2);
        assertTrue(w3CBuilder.namespaces.containsKey("prefix"));
        assertEquals("namespace", w3CBuilder.namespaces.get("prefix"));

        // Test when the element does not contain any namespace attribute
        org.jsoup.nodes.Element el3 = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        String prefix3 = w3CBuilder.updateNamespaces(el3);
        assertEquals("", prefix3);
        assertFalse(w3CBuilder.namespaces.containsKey(""));
    }

    @Test
    public void testAsString() {
        // Test when the input W3C document is not null
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            fail("Expected no exception to be thrown");
        }
        assertNotNull(w3CDom.asString(doc));

        // Test when the input W3C document is null
        try {
            assertNull(w3CDom.asString(null));
        } catch (NullPointerException e) {
            assertEquals("The input W3C document cannot be null", e.getMessage());
        }
    }
}