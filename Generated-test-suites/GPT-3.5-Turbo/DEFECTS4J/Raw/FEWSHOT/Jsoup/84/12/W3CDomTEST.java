package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            Document convertedDoc = w3cDom.fromJsoup(jDoc);
            assertEquals(w3cDoc, convertedDoc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            w3cDom.convert(jDoc, w3cDoc);
            assertNotNull(w3cDoc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testW3CBuilderHead() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
            
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "test");
            org.jsoup.nodes.Node sourceNode = new org.jsoup.nodes.Node(org.jsoup.parser.Tag.valueOf("test"), "test");
            
            w3cBuilder.head(sourceEl, 1);
            Element resultEl = w3cBuilder.dest;
            
            assertEquals("test", resultEl.getTagName());
            assertTrue(resultEl instanceof Element);
            
            w3cBuilder.head(sourceNode, 1);
            assertTrue(sourceNode instanceof TextNode);
            
            w3cBuilder.head(sourceNode, 1);
            assertTrue(sourceNode instanceof Comment);
            
            w3cBuilder.head(sourceNode, 1);
            assertTrue(sourceNode instanceof org.jsoup.nodes.DataNode);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testW3CBuilderTail() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
            
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "test");
            
            w3cBuilder.tail(sourceEl, 1);
            assertNull(w3cBuilder.dest);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCopyAttributes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
            
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "test");
            sourceEl.attributes().put("attr1", "value1");
            sourceEl.attributes().put("attr2", "value2");
            
            Element destEl = w3cDoc.createElement("test");
            
            w3cBuilder.copyAttributes(sourceEl, destEl);
            
            assertEquals("value1", destEl.getAttribute("attr1"));
            assertEquals("value2", destEl.getAttribute("attr2"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdateNamespaces() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
            
            org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "test");
            el.attributes().put("xmlns:test", "urn:test");
            
            String result = w3cBuilder.updateNamespaces(el);
            
            assertEquals("test", result);
            assertTrue(w3cBuilder.namespacesStack.peek().containsKey("test"));
            assertEquals("urn:test", w3cBuilder.namespacesStack.peek().get("test"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            
            String result = w3cDom.asString(w3cDoc);
            
            assertNotNull(result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}