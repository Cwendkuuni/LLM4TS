
package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private TransformerFactory transformerFactory;
    private Transformer transformer;

    public W3CDomTest() throws ParserConfigurationException, TransformerException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
    }

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() throws ParserConfigurationException, SAXException, IOException {
        org.jsoup.nodes.Document jsoupDoc = Document.createShell("");
        Element root = jsoupDoc.appendElement("html");
        Element body = root.appendElement("body");
        Element div = body.appendElement("div");
        TextNode text = new TextNode("Hello World");
        Comment comment = new Comment("This is a comment");

        div.appendChild(text);
        div.appendChild(comment);

        Document w3cDoc = builder.newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        NodeList nodeList = w3cDoc.getElementsByTagName("div");
        assertEquals(1, nodeList.getLength());
        Node divNode = nodeList.item(0);
        
        NodeList children = divNode.getChildNodes();
        assertEquals(2, children.getLength());
        assertTrue(children.item(0).getNodeName().equals("#text"));
        assertTrue(children.item(1).getNodeName().equals("#comment"));
    }

    @Test
    public void testW3CBuilder() throws ParserConfigurationException, TransformerException {
        Document w3cDoc = builder.newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        
        org.jsoup.nodes.Document jsoupDoc = Document.createShell("");
        Element root = jsoupDoc.appendElement("html");
        Element body = root.appendElement("body");
        Element div = body.appendElement("div");
        Attribute attr = new Attribute("class", "container");

        div.attributes().put(attr);
        w3cBuilder.head(div, 0);
        
        NodeList nodeList = w3cDoc.getElementsByTagName("div");
        assertEquals(1, nodeList.getLength());
        Node divNode = nodeList.item(0);
        
        NodeList attributes = divNode.getAttributes();
        assertEquals(1, attributes.getLength());
        Node attribute = attributes.item(0);
        assertEquals("class", attribute.getNodeName());
        assertEquals("container", attribute.getNodeValue());
        
        w3cBuilder.tail(div, 0);
    }

    @Test
    public void testCopyAttributes() throws ParserConfigurationException, TransformerException {
        Document w3cDoc = builder.newDocument();
        Element w3cEl = w3cDoc.createElement("div");

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        Attribute attr1 = new Attribute("class", "container");
        Attribute attr2 = new Attribute("data-id", "123");
        Attribute attr3 = new Attribute("data-name", "test");
        
        jsoupEl.attributes().put(attr1);
        jsoupEl.attributes().put(attr2);
        jsoupEl.attributes().put(attr3);
        
        new W3CDom.W3CBuilder(w3cDoc).copyAttributes(jsoupEl, w3cEl);

        assertEquals(3, w3cEl.getAttributes().getLength());
        assertEquals("class", w3cEl.getAttributes().item(0).getNodeName());
        assertEquals("container", w3cEl.getAttributes().item(0).getNodeValue());
        assertEquals("data-id", w3cEl.getAttributes().item(1).getNodeName());
        assertEquals("123", w3cEl.getAttributes().item(1).getNodeValue());
        assertEquals("data-name", w3cEl.getAttributes().item(2).getNodeName());
        assertEquals("test", w3cEl.getAttributes().item(2).getNodeValue());
    }

    @Test
    public void testUpdateNamespaces() throws ParserConfigurationException, TransformerException {
        Document w3cDoc = builder.newDocument();
        Element w3cEl = w3cDoc.createElement("div");

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        Attribute attr1 = new Attribute("xmlns", "http://www.w3.org/1999/xhtml");
        Attribute attr2 = new Attribute("xmlns:custom", "http://www.example.com");

        jsoupEl.attributes().put(attr1);
        jsoupEl.attributes().put(attr2);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = w3cBuilder.updateNamespaces(jsoupEl);

        assertEquals("", prefix);
        assertEquals(2, w3cBuilder.namespacesStack.size());
        assertEquals("http://www.w3.org/1999/xhtml", w3cBuilder.namespacesStack.peek().get(""));
        assertEquals("http://www.example.com", w3cBuilder.namespacesStack.peek().get("custom"));
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        Document w3cDoc = builder.newDocument();
        Element root = w3cDoc.createElement("root");
        TextNode text = new TextNode("Hello World");
        Comment comment = w3cDoc.createComment("This is a comment");

        root.appendChild(text);
        root.appendChild(comment);
        w3cDoc.appendChild(root);

        String w3cDocString = new W3CDom().asString(w3cDoc);
        String expectedXml = XML_HEADER + "<root>Hello World<!--This is a comment--></root>";
        
        assertEquals(expectedXml, w3cDocString);
    }
}
