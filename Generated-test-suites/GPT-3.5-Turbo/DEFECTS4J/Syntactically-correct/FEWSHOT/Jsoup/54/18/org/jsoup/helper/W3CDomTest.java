
package org.jsoup.helper;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DummyNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3CDom;
    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws ParserConfigurationException {
        w3CDom = new W3CDom();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    @Test
    public void testFromJsoup() {
        // Creating a dummy jsoup document 
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        DummyNode rootNode = new DummyNode("");
        DummyNode childNode = new DummyNode("childNode");
        DummyNode grandChildNode = new DummyNode("grandChildNode");
        DummyNode grandGrandChildNode = new DummyNode("grandGrandChildNode");
        jsoupDocument.appendChild(rootNode);
        rootNode.appendChild(childNode);
        childNode.appendChild(grandChildNode);
        grandChildNode.appendChild(grandGrandChildNode);

        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        assertTrue(w3cDocument instanceof org.w3c.dom.Document);
        org.w3c.dom.Element rootElement = w3cDocument.getDocumentElement();
        assertEquals("rootNode", rootElement.getTagName());
        assertEquals(1, rootElement.getChildNodes().getLength());
        org.w3c.dom.Node childElement = rootElement.getChildNodes().item(0);
        assertEquals("childNode", childElement.getNodeName());
        assertEquals(1, childElement.getChildNodes().getLength());
        org.w3c.dom.Node grandChildElement = childElement.getChildNodes().item(0);
        assertEquals("grandChildNode", grandChildElement.getNodeName());
        assertEquals(1, grandChildElement.getChildNodes().getLength());
        org.w3c.dom.Node grandGrandChildElement = grandChildElement.getChildNodes().item(0);
        assertEquals("grandGrandChildNode", grandGrandChildElement.getNodeName());
        assertEquals(0, grandGrandChildElement.getChildNodes().getLength());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        DummyNode rootNode = new DummyNode("");
        DummyNode childNode = new DummyNode("childNode");
        DummyNode grandChildNode = new DummyNode("grandChildNode");
        DummyNode grandGrandChildNode = new DummyNode("grandGrandChildNode");
        jsoupDocument.appendChild(rootNode);
        rootNode.appendChild(childNode);
        childNode.appendChild(grandChildNode);
        grandChildNode.appendChild(grandGrandChildNode);

        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        w3CDom.convert(jsoupDocument, w3cDocument);

        org.w3c.dom.Element rootElement = w3cDocument.getDocumentElement();
        assertEquals("rootNode", rootElement.getTagName());
        assertEquals(1, rootElement.getChildNodes().getLength());
        org.w3c.dom.Node childElement = rootElement.getChildNodes().item(0);
        assertEquals("childNode", childElement.getNodeName());
        assertEquals(1, childElement.getChildNodes().getLength());
        org.w3c.dom.Node grandChildElement = childElement.getChildNodes().item(0);
        assertEquals("grandChildNode", grandChildElement.getNodeName());
        assertEquals(1, grandChildElement.getChildNodes().getLength());
        org.w3c.dom.Node grandGrandChildElement = grandChildElement.getChildNodes().item(0);
        assertEquals("grandGrandChildNode", grandGrandChildElement.getNodeName());
        assertEquals(0, grandGrandChildElement.getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element parentElement = w3cDocument.createElement("parentElement");
        w3cDocument.appendChild(parentElement);

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        Element jsoupElement = new Element("jsoupElement");
        w3CBuilder.head(jsoupElement, 0);

        assertEquals(1, parentElement.getChildNodes().getLength());
        org.w3c.dom.Node childElement = parentElement.getChildNodes().item(0);
        assertEquals("jsoupElement", childElement.getNodeName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element parentElement = w3cDocument.createElement("parentElement");
        w3cDocument.appendChild(parentElement);

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        TextNode jsoupTextNode = new TextNode("textNode", "");
        w3CBuilder.head(jsoupTextNode, 0);

        assertEquals(1, parentElement.getChildNodes().getLength());
        org.w3c.dom.Node childElement = parentElement.getChildNodes().item(0);
        assertEquals("textNode", childElement.getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element parentElement = w3cDocument.createElement("parentElement");
        w3cDocument.appendChild(parentElement);

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        Comment jsoupComment = new Comment("jsoupComment", "");
        w3CBuilder.head(jsoupComment, 0);

        assertEquals(1, parentElement.getChildNodes().getLength());
        org.w3c.dom.Node childElement = parentElement.getChildNodes().item(0);
        assertEquals("jsoupComment", childElement.getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element parentElement = w3cDocument.createElement("parentElement");
        w3cDocument.appendChild(parentElement);

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("jsoupDataNode", "");
        w3CBuilder.head(jsoupDataNode, 0);

        assertEquals(1, parentElement.getChildNodes().getLength());
        org.w3c.dom.Node childElement = parentElement.getChildNodes().item(0);
        assertEquals("jsoupDataNode", childElement.getNodeValue());
    }

    @Test
    public void testW3CBuilderTailElement() {
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element parentElement = w3cDocument.createElement("parentElement");
        w3cDocument.appendChild(parentElement);
        org.w3c.dom.Element childElement = w3cDocument.createElement("childElement");
        parentElement.appendChild(childElement);

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3CBuilder.tail(new Element("jsoupElement"), 0);

        assertEquals(0, parentElement.getChildNodes().getLength());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("jsoupElement");
        jsoupElement.attr("attr1", "value1");
        jsoupElement.attr("attr2", "value2");
        jsoupElement.attr("attr3", "value3");

        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element w3cElement = w3cDocument.createElement("w3cElement");

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        w3CBuilder.copyAttributes(jsoupElement, w3cElement);

        assertEquals("value1", w3cElement.getAttribute("attr1"));
        assertEquals("value2", w3cElement.getAttribute("attr2"));
        assertEquals("value3", w3cElement.getAttribute("attr3"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("jsoupElement");
        jsoupElement.attr("xmlns", "namespace1");
        jsoupElement.attr("xmlns:test", "namespace2");

        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element w3cElement = w3cDocument.createElement("w3cElement");

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(w3cDocument);
        String prefix = w3CBuilder.updateNamespaces(jsoupElement);

        assertEquals(2, w3CBuilder.namespaces.size());
        assertEquals("namespace1", w3CBuilder.namespaces.get(""));
        assertEquals("namespace2", w3CBuilder.namespaces.get("test"));
        assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        org.w3c.dom.Element rootElement = w3cDocument.createElement("rootElement");
        w3cDocument.appendChild(rootElement);
        org.w3c.dom.Element childElement1 = w3cDocument.createElement("childElement1");
        org.w3c.dom.Element childElement2 = w3cDocument.createElement("childElement2");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);

        String result = w3CDom.asString(w3cDocument);

        assertTrue(result.contains("<rootElement>"));
        assertTrue(result.contains("<childElement1>"));
        assertTrue(result.contains("<childElement2>"));
    }
}
