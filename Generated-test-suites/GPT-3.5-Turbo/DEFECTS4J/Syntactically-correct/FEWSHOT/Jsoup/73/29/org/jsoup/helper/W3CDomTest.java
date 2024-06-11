package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
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
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNull() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertNotNull(w3cDoc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertWithLocation() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
            jsoupDoc.location("http://example.com");
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertEquals("http://example.com", w3cDoc.getDocumentURI());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertWithRootElement() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
            Element rootElement = new Element("html");
            rootElement.setBaseUri("http://example.com");
            jsoupDoc.appendChild(rootElement);
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertNotNull(w3cDoc.getDocumentElement());
            assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHeadWithElement() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
            sourceEl.attr("id", "test");
            builder.head(sourceEl, 0);
            assertNotNull(w3cDoc.getDocumentElement().getAttribute("id"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHeadWithTextNode() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("This is a test");
            builder.head(sourceText, 0);
            assertNotNull(w3cDoc.getTextContent());
            assertEquals("This is a test", w3cDoc.getTextContent());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHeadWithComment() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("This is a test");
            builder.head(sourceComment, 0);
            assertNotNull(w3cDoc.getComment());
            assertEquals("This is a test", w3cDoc.getComment());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHeadWithDataNode() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("This is a test");
            builder.head(sourceData, 0);
            assertNotNull(w3cDoc.getTextContent());
            assertEquals("This is a test", w3cDoc.getTextContent());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderTailWithElement() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
            builder.head(sourceEl, 0);
            builder.tail(sourceEl, 0);
            assertNull(builder.dest);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyAttributes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
            sourceEl.attr("id", "test");
            Element destEl = w3cDoc.createElementNS("", "div");
            builder.copyAttributes(sourceEl, destEl);
            assertNotNull(destEl.getAttribute("id"));
            assertEquals("test", destEl.getAttribute("id"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyAttributesWithInvalidKeys() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
            sourceEl.attr("id", "test");
            sourceEl.attr("test-key", "test-value");
            sourceEl.attr(":key:", "test-value");
            sourceEl.attr("invalid:key", "test-value");
            Element destEl = w3cDoc.createElementNS("", "div");
            builder.copyAttributes(sourceEl, destEl);
            assertNotNull(destEl.getAttribute("id"));
            assertEquals("test", destEl.getAttribute("id"));
            assertNull(destEl.getAttribute("test-key"));
            assertNull(destEl.getAttribute(":key:"));
            assertNull(destEl.getAttribute("invalid:key"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateNamespaces() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
            el.attr("xmlns", "http://example.com");
            el.attr("xmlns:prefix", "http://example.com/prefix");
            el.attr("xmlns:test", "http://example.com/test");
            builder.updateNamespaces(el);
            assertEquals("http://example.com", builder.namespaces.get(""));
            assertEquals("http://example.com/prefix", builder.namespaces.get("prefix"));
            assertEquals("http://example.com/test", builder.namespaces.get("test"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            Element el = w3cDoc.createElementNS("", "div");
            w3cDoc.appendChild(el);
            String result = w3cDom.asString(w3cDoc);
            assertNotNull(result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringTransformerException() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            Element el = w3cDoc.createElementNS("", "div");
            w3cDoc.appendChild(el);
            w3cDom.asString(null);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}