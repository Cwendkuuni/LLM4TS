package org.jsoup.helper;

import org.junit.jupiter.api.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.w3c.dom.*;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class W3CDomTest {

    private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
    private static final String HTML_NS = "http://www.w3.org/1999/xhtml";

    @BeforeAll
    static void initFactory() {
        W3CDom.W3CDomHelper helper = new W3CDom.W3CDomHelper();
        helper.setFactory(W3CDom.W3CDomHelper.createDocumentBuilderFactory());
    }

    @Test
    void testFromJsoup() {
        Document jsoupDocument = new TextDocument("");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    void testConvert() {
        Document jsoupDocument = new TextDocument("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element root = w3cDocument.getDocumentElement();
        assertEquals("html", root.getTagName());
        Element body = root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());
        Element h1 = body.getElementsByTagName("h1").item(0);
        assertEquals("h1", h1.getTagName());
        assertEquals("Hello World", h1.getTextContent());
    }

    @Test
    void testAsString() {
        Document document = createTestDocument();
        String w3cString = new W3CDom().asString(document);
        String jsoupString = document.toString();
        assertEquals(jsoupString, w3cString);
    }

    @Test
    void testFromJsoupNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new W3CDom().fromJsoup(null));
    }

    @Test
    void testConvertNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new W3CDom().convert(null, null));
    }

    @Test
    void testConvertNullOutput() {
        Document jsoupDocument = new TextDocument("");
        assertThrows(NullPointerException.class, () -> new W3CDom().convert(jsoupDocument, null));
    }

    @Test
    void testNamespaces() {
        Document document = createTestDocumentWithNamespaces();
        new W3CDom().convert(document, null);

        DocumentTraversal traversal = document.createDocumentTraversal();
        NodeFilter filter = document.createElementFilter(":ns-prefix('xml')");
        NodeList xmlNodes = traversal.traverse(document, filter, null, NodeFilter.SHOW_ELEMENT);
        assertEquals(1, xmlNodes.getLength());
        Element xmlElement = (Element) xmlNodes.item(0);
        assertEquals("xml", xmlElement.getTagName());
        assertEquals(XML_NS, xmlElement.getAttribute("xmlns"));

        NodeList htmlNodes = traversal.traversal(document, filter, null, NodeFilter.SHOW_ELEMENT);
        assertEquals(1, htmlNodes.getLength());
        Element htmlElement = (Element) htmlNodes.item(0);
        assertEquals("html", htmlElement.getTagName());
        assertEquals(HTML_NS, htmlElement.getAttribute("xmlns"));
    }

    @Test
    void testConvertWithCustomNamespace() {
        Document document = createTestDocumentWithCustomNamespace();
        new W3CDom().convert(document, null);

        DocumentTraversal traversal = document.createDocumentTraversal();
        NodeFilter filter = document.createElementFilter(":ns-prefix('my')");
        NodeList myNodes = traversal.traverse(document, filter, null, NodeFilter.SHOW_ELEMENT);
        assertEquals(1, myNodes.getLength());
        Element myElement = (Element) myNodes.item(0);
        assertEquals("myElement", myElement.getTagName());
        assertEquals("http://example.com/ns", myElement.getAttribute("xmlns:my"));
    }

    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            return document;
        } catch (ParserConfigurationException e) {
            throw new AssertionError(e);
        }
    }

    private Document createTestDocumentWithNamespaces() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);
            document.setDocumentURI("http://example.com/test.xml");
            document.createElementNS(XML_NS, "xml");
            document.createElementNS(HTML_NS, "html");
            Element html = document.getDocumentElement();
            html.setAttribute("xmlns", HTML_NS);
            html.setAttribute("xmlns:xml", XML_NS);
            Element body = document.createElement("body");
            Element h1 = document.createElement("h1");
            h1.setTextContent("Hello World");
            body.appendChild(h1);
            html.appendChild(body);
            return document;
        } catch (ParserConfigurationException e) {
            throw new AssertionError(e);
        }
    }

    private Document createTestDocumentWithCustomNamespace() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);
            document.setDocumentURI("http://example.com/test.xml");
            Element xml = document.createElementNS("http://example.com/ns", "xml");
            xml.setAttribute("xmlns:my", "http://example.com/ns/my");
            document.appendChild(xml);
            Element myElement = document.createElementNS("http://example.com/ns/my", "myElement");
            xml.appendChild(myElement);
            myElement.setTextContent("Hello World");
            return document;
        } catch (ParserConfigurationException e) {
            throw new AssertionError(e);
        }
    }
}