package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        Element child1 = doc.createElement("child1");
        root.appendChild(child1);
        
        Element child2 = doc.createElement("child2");
        root.appendChild(child2);
        
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendChild(jsoupDoc.createRootElement());
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        NodeList nodeList = w3cDoc.getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node rootNode = nodeList.item(0);
        assertTrue(rootNode instanceof Element);
        assertEquals("root", rootNode.getNodeName());
        
        NodeList childNodes = rootNode.getChildNodes();
        assertEquals(2, childNodes.getLength());
        Node childNode1 = childNodes.item(0);
        assertTrue(childNode1 instanceof Element);
        assertEquals("child1", childNode1.getNodeName());
        
        Node childNode2 = childNodes.item(1);
        assertTrue(childNode2 instanceof Element);
        assertEquals("child2", childNode2.getNodeName());
    }

    @Test
    public void testConvert() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        Element child1 = doc.createElement("child1");
        root.appendChild(child1);
        
        Element child2 = doc.createElement("child2");
        root.appendChild(child2);
        
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendChild(jsoupDoc.createRootElement());
        
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, doc);
        
        NodeList nodeList = doc.getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node rootNode = nodeList.item(0);
        assertTrue(rootNode instanceof Element);
        assertEquals("root", rootNode.getNodeName());
        
        NodeList childNodes = rootNode.getChildNodes();
        assertEquals(2, childNodes.getLength());
        Node childNode1 = childNodes.item(0);
        assertTrue(childNode1 instanceof Element);
        assertEquals("child1", childNode1.getNodeName());
        
        Node childNode2 = childNodes.item(1);
        assertTrue(childNode2 instanceof Element);
        assertEquals("child2", childNode2.getNodeName());
    }

    @Test
    public void testW3CBuilderHead() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("element");
        sourceElement.setBaseUri("http://www.example.com");
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        sourceElement.setAttributes(attributes);
        
        builder.head(sourceElement, 0);
        
        NodeList nodeList = doc.getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node rootNode = nodeList.item(0);
        assertTrue(rootNode instanceof Element);
        assertEquals("root", rootNode.getNodeName());
        
        NodeList childNodes = rootNode.getChildNodes();
        assertEquals(1, childNodes.getLength());
        Node childNode = childNodes.item(0);
        assertTrue(childNode instanceof Element);
        assertEquals("element", childNode.getNodeName());
        
        org.w3c.dom.Element w3cElement = (org.w3c.dom.Element) childNode;
        assertEquals("http://www.example.com", w3cElement.getBaseURI());
        
        assertEquals(2, w3cElement.getAttributes().getLength());
        assertEquals("attr1", w3cElement.getAttribute("attr1"));
        assertEquals("attr2", w3cElement.getAttribute("attr2"));
    }

    @Test
    public void testW3CBuilderTail() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        
        org.jsoup.nodes.Element sourceElement1 = new org.jsoup.nodes.Element("element1");
        org.jsoup.nodes.Element sourceElement2 = new org.jsoup.nodes.Element("element2");
        
        builder.head(sourceElement1, 0);
        builder.head(sourceElement2, 1);
        
        builder.tail(sourceElement2, 1);
        
        NodeList nodeList = doc.getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node rootNode = nodeList.item(0);
        assertTrue(rootNode instanceof Element);
        assertEquals("root", rootNode.getNodeName());
        
        NodeList childNodes = rootNode.getChildNodes();
        assertEquals(1, childNodes.getLength());
        Node childNode = childNodes.item(0);
        assertTrue(childNode instanceof Element);
        assertEquals("element1", childNode.getNodeName());
    }

    @Test
    public void testW3CBuilderTailWithParentNodeNotElement() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        Element child = doc.createElement("child");
        child.appendChild(doc.createTextNode("text"));
        root.appendChild(child);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.dest = child;
        builder.tail(child, 1);
        
        NodeList nodeList = doc.getChildNodes();
        assertEquals(1, nodeList.getLength());
        Node rootNode = nodeList.item(0);
        assertTrue(rootNode instanceof Element);
        assertEquals("root", rootNode.getNodeName());
        
        NodeList childNodes = rootNode.getChildNodes();
        assertEquals(1, childNodes.getLength());
        Node childNode = childNodes.item(0);
        assertTrue(childNode instanceof Text);
        assertEquals("text", childNode.getNodeValue());
    }

    @Test
    public void testCopyAttributes() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("element");
        sourceElement.setBaseUri("http://www.example.com");
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        sourceElement.setAttributes(attributes);
        
        Element destElement = doc.createElement("element");
        builder.copyAttributes(sourceElement, destElement);
        
        assertEquals(2, destElement.getAttributes().getLength());
        assertEquals("attr1", destElement.getAttribute("attr1"));
        assertEquals("attr2", destElement.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("element");
        Attributes attributes = new Attributes();
        attributes.put("xmlns", "http://www.example.com");
        attributes.put("xmlns:prefix", "http://www.example.com/prefix");
        sourceElement.setAttributes(attributes);
        
        String prefix = builder.updateNamespaces(sourceElement);
        
        assertEquals(2, builder.namespaces.size());
        assertEquals("http://www.example.com", builder.namespaces.get(""));
        assertEquals("http://www.example.com/prefix", builder.namespaces.get("prefix"));
        
        assertEquals("prefix", prefix);
    }

    @Test
    public void testAsString() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(doc);
        
        assertTrue(result.startsWith("<?xml"));
        assertTrue(result.contains("<root>"));
    }
}