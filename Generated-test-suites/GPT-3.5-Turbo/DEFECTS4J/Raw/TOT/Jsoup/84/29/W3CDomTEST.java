package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;
import org.w3c.dom.Document;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3c;

    @Before
    public void setUp() {
        w3c = new W3CDom();
    }

    @Test
    public void testFromJsoup_typicalCase() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3c.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(w3cDoc.getImplementation().getClass(), org.apache.xerces.dom.DocumentImpl.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullInput() {
        w3c.fromJsoup(null);
    }
    
    @Test
    public void testConvert_typicalCase() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendChild(new Element("body").text("Hello, World!"));
        Document w3cDoc = w3c.fromJsoup(jsoupDoc);

        Document output = createEmptyW3CDocument();
        w3c.convert(jsoupDoc, output);

        assertNotNull(output.getDocumentElement());
        assertNotNull(output.getDocumentURI());

        Element rootElement = output.getDocumentElement();
        assertEquals(rootElement.getNodeName(), "body");
        assertEquals(rootElement.getChildNodes().getLength(), 1);

        Text textNode = (Text) rootElement.getChildNodes().item(0);
        assertEquals(textNode.getWholeText(), "Hello, World!");
    }

    @Test
    public void testConvert_jsoupLocation() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Document output = createEmptyW3CDocument();
        w3c.convert(jsoupDoc, output);
        assertEquals(output.getDocumentURI(), "http://example.com");
    }

    @Test
    public void testW3CBuilder_head_elementTypicalCase() {
        Document doc = createEmptyW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        builder.head(element, 0);

        assertNotNull(builder.dest);
        assertEquals(builder.dest.getNodeName(), "div");
    }

    @Test
    public void testW3CBuilder_head_elementWithText() {
        Document doc = createEmptyW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("p").text("Hello, World!");
        builder.head(element, 0);

        assertNotNull(builder.dest);
        assertEquals(builder.dest.getNodeName(), "p");
        assertEquals(builder.dest.getChildNodes().getLength(), 1);

        Text textNode = (Text) builder.dest.getChildNodes().item(0);
        assertEquals(textNode.getWholeText(), "Hello, World!");
    }

    @Test
    public void testW3CBuilder_head_elementWithComment() {
        Document doc = createEmptyW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("This is a comment");
        builder.head(comment, 0);

        assertNotNull(builder.dest);
        assertEquals(builder.dest.getNodeType(), org.w3c.dom.Node.COMMENT_NODE);

        Comment commentNode = (Comment) builder.dest;
        assertEquals(commentNode.getData(), "This is a comment");
    }

    @Test
    public void testW3CBuilder_head_elementWithData() {
        Document doc = createEmptyW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.DataNode data = new org.jsoup.nodes.DataNode("foo", "This is some data");
        builder.head(data, 0);

        assertNotNull(builder.dest);
        assertEquals(builder.dest.getNodeType(), org.w3c.dom.Node.TEXT_NODE);

        Text textNode = (Text) builder.dest;
        assertEquals(textNode.getData(), "This is some data");
    }

    @Test
    public void testW3CBuilder_tail() {
        Document doc = createEmptyW3CDocument();
        NodeTraversor traversor = new NodeTraversor(new W3CDom.W3CBuilder(doc));

        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        traversor.head(element, 0);
        traversor.tail(element, 0);

        assertNull(((W3CDom.W3CBuilder) traversor.getVisitor()).dest);
    }
    
    @Test
    public void testW3CBuilder_copyAttributes() {
        Document doc = createEmptyW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("id", "myDiv");
        element.attr("class", "myClass");

        Element w3cElement = doc.createElementNS("", "div");
        builder.copyAttributes(element, w3cElement);

        assertNotNull(w3cElement.getAttributeNode("id"));
        assertEquals(w3cElement.getAttributeNode("id").getValue(), "myDiv");
        assertNotNull(w3cElement.getAttributeNode("class"));
        assertEquals(w3cElement.getAttributeNode("class").getValue(), "myClass");
    }

    @Test
    public void testW3CBuilder_updateNamespaces() {
        Document doc = createEmptyW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("xmlns", "http://example.com");
        element.attr("xmlns:foo", "http://example.com/foo");

        String prefix = builder.updateNamespaces(element);

        assertEquals(prefix, "");
        Map<String, String> namespaceMap = builder.namespacesStack.peek();
        assertTrue(namespaceMap.containsKey(""));
        assertEquals(namespaceMap.get(""), "http://example.com");
        assertTrue(namespaceMap.containsKey("foo"));
        assertEquals(namespaceMap.get("foo"), "http://example.com/foo");
    }
   
    @Test
    public void testAsString_typicalCase() {
        Document doc = createEmptyW3CDocument();
        Element element = doc.createElement("p");
        element.setTextContent("Hello, World!");
        doc.appendChild(element);

        String output = w3c.asString(doc);
        assertNotNull(output);
        assertEquals(output, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><p>Hello, World!</p>");
    }

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
}