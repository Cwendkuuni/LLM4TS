package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        // test conversion with empty jsoup document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // test conversion with non-empty jsoup document
        org.jsoup.nodes.Element root = new org.jsoup.nodes.Element("root");
        jsoupDoc.appendChild(root);
        
        org.jsoup.nodes.Element el1 = new org.jsoup.nodes.Element("el1");
        root.appendChild(el1);
        
        org.jsoup.nodes.TextNode text = new org.jsoup.nodes.TextNode("text", "");
        el1.appendChild(text);
        
        Document w3cDoc2 = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc2);
        
        Element rootElement = w3cDoc2.getDocumentElement();
        assertNotNull(rootElement);
        
        Element el1Element = (Element)rootElement.getElementsByTagName("el1").item(0);
        assertNotNull(el1Element);
        
        Node textNode = el1Element.getFirstChild();
        assertNotNull(textNode);
        assertTrue(textNode instanceof Text);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        // test conversion with empty jsoup document
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        
        // test conversion with non-empty jsoup document
        org.jsoup.nodes.Element root = new org.jsoup.nodes.Element("root");
        jsoupDoc.appendChild(root);
        
        org.jsoup.nodes.Element el1 = new org.jsoup.nodes.Element("el1");
        root.appendChild(el1);
        
        org.jsoup.nodes.TextNode text = new org.jsoup.nodes.TextNode("text", "");
        el1.appendChild(text);
        
        Document w3cDoc2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc2);
        
        Element rootElement2 = w3cDoc2.getDocumentElement();
        assertNotNull(rootElement2);
        
        Element el1Element2 = (Element)rootElement2.getElementsByTagName("el1").item(0);
        assertNotNull(el1Element2);
        
        Node textNode2 = el1Element2.getFirstChild();
        assertNotNull(textNode2);
        assertTrue(textNode2 instanceof Text);
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        // test head method
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("sourceEl");
        builder.head(sourceEl, 1);
        Element destEl = w3cDoc.getDocumentElement();
        assertEquals("sourceEl", destEl.getTagName());
        
        // test tail method
        builder.tail(sourceEl, 1);
        Element parentEl = (Element)destEl.getParentNode();
        assertNotNull(parentEl);
        assertEquals("root", parentEl.getTagName());
        
        // test head method with different node types
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("comment");
        builder.head(comment, 2);
        Node commentNode = parentEl.getFirstChild();
        assertNotNull(commentNode);
        assertTrue(commentNode instanceof org.w3c.dom.Comment);
        
        org.jsoup.nodes.TextNode text = new org.jsoup.nodes.TextNode("text", "");
        builder.head(text, 2);
        Node textNode = parentEl.getLastChild();
        assertNotNull(textNode);
        assertTrue(textNode instanceof org.w3c.dom.Text);
        
        org.jsoup.nodes.DataNode data = new org.jsoup.nodes.DataNode("data", "");
        builder.head(data, 2);
        Node dataNode = parentEl.getLastChild();
        assertNotNull(dataNode);
        assertTrue(dataNode instanceof org.w3c.dom.Text);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("sourceEl");
        sourceEl.attr("class", "test");
        
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element destEl = w3cDoc.createElement("destEl");
        
        // test copy attributes method
        new W3CDom().convert(sourceEl, w3cDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.copyAttributes(sourceEl, destEl);
        
        assertEquals("test", destEl.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("el");
        el.attr("xmlns", "namespace");
        el.attr("xmlns:prefix", "namespace");
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        String prefix = builder.updateNamespaces(el);
        assertNotNull(prefix);
        assertEquals("", prefix);
        
        el.attr("xmlns:prefix2", "namespace2");
        prefix = builder.updateNamespaces(el);
        assertNotNull(prefix);
        assertEquals("prefix2", prefix);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        
        // test asString method
        String str = new W3CDom().asString(w3cDoc);
        assertNotNull(str);
    }

}