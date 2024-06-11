package org.jsoup.helper;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tag", "", attributes);
        jsoupDocument.appendChild(element);

        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        Element w3cElement = w3cDocument.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("tag", w3cElement.getTagName());
        assertEquals("value1", w3cElement.getAttribute("key1"));
        assertEquals("value2", w3cElement.getAttribute("key2"));
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tag", "", attributes);
        jsoupDocument.appendChild(element);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDocument, w3cDocument);

        Element w3cElement = w3cDocument.getDocumentElement();
        assertNotNull(w3cElement);
        assertEquals("tag", w3cElement.getTagName());
        assertEquals("value1", w3cElement.getAttribute("key1"));
        assertEquals("value2", w3cElement.getAttribute("key2"));
    }

    @Test
    public void testW3CBuilderHead() throws ParserConfigurationException {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tag");
        org.jsoup.nodes.Node source = element;
        int depth = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();
        Element dest = w3cDocument.createElement("root");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.head(source, depth);

        Element w3cElement = (Element) dest.getFirstChild();
        assertNotNull(w3cElement);
        assertEquals("tag", w3cElement.getTagName());
    }

    @Test
    public void testW3CBuilderTail() throws ParserConfigurationException {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tag");
        org.jsoup.nodes.Node source = element;
        int depth = 1;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();
        Element dest = w3cDocument.createElement("root");
        Element parent = w3cDocument.createElement("parent");
        parent.appendChild(dest);
        w3cDocument.appendChild(parent);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.tail(source, depth);

        Element w3cElement = (Element) dest.getParentNode();
        assertNotNull(w3cElement);
        assertEquals("parent", w3cElement.getTagName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() throws ParserConfigurationException {
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("text", "");
        org.jsoup.nodes.Node source = textNode;
        int depth = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();
        Element dest = w3cDocument.createElement("root");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.head(source, depth);

        Text w3cText = (Text) dest.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("text", w3cText.getData());
    }

    @Test
    public void testW3CBuilderHeadComment() throws ParserConfigurationException {
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("comment", "");
        org.jsoup.nodes.Node source = comment;
        int depth = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();
        Element dest = w3cDocument.createElement("root");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.head(source, depth);

        Comment w3cComment = (Comment) dest.getFirstChild();
        assertNotNull(w3cComment);
        assertEquals("comment", w3cComment.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() throws ParserConfigurationException {
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("data", "");
        org.jsoup.nodes.Node source = dataNode;
        int depth = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();
        Element dest = w3cDocument.createElement("root");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.head(source, depth);

        Text w3cText = (Text) dest.getFirstChild();
        assertNotNull(w3cText);
        assertEquals("data", w3cText.getData());
    }

    @Test
    public void testW3CBuilderHeadUnsupportedNode() throws ParserConfigurationException {
        org.jsoup.nodes.Node unsupportedNode = new org.jsoup.nodes.Node("");
        org.jsoup.nodes.Node source = unsupportedNode;
        int depth = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDocument = builder.newDocument();
        Element dest = w3cDocument.createElement("root");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3cBuilder.head(source, depth);

        assertTrue(dest.getChildNodes().getLength() == 0);
    }

    @Test
    public void testCopyAttributes() throws ParserConfigurationException {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tag");
        element.attr("key", "value");
        org.jsoup.nodes.Node source = element;
        Element dest = new DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("root");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        w3cBuilder.copyAttributes(source, dest);

        assertEquals("value", dest.getAttribute("key"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("tag");
        element.attr("xmlns", "urn");
        org.jsoup.nodes.Node source = element;
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        w3cBuilder.updateNamespaces(element);

        assertTrue(w3cBuilder.namespaces.size() == 1);
        assertEquals("urn", w3cBuilder.namespaces.get(""));
    }

    @Test
    public void testAsString() throws ParserConfigurationException {
        Document doc = new DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        W3CDom w3cDom = new W3CDom();
        String w3cString = "<root/>";

        assertEquals(w3cString, w3cDom.asString(doc));
    }
}