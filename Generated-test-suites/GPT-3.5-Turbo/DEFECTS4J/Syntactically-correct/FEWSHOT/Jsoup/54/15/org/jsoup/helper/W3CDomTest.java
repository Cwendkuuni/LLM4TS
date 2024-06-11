package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import org.w3c.dom.Document;
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
    
    private static final StringUtil StringUtil = new StringUtil();
    
    @Test
    public void testFromJsoup() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        jsoupDocument.location("https://example.com");

        W3CDom w3CDom = new W3CDom();
        Document out = w3CDom.fromJsoup(jsoupDocument);

        assertNull(document.getDoctype());
        assertNull(document.getNamespaceURI());
        assertEquals("https://example.com", document.getDocumentURI());
    }
    
    @Test
    public void testConvert() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        jsoupDocument.location("https://example.com");

        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        jsoupDocument.appendChild(rootElement);

        org.jsoup.nodes.Element divElement = new org.jsoup.nodes.Element("div");
        rootElement.appendChild(divElement);

        org.jsoup.nodes.Element spanElement = new org.jsoup.nodes.Element("span");
        divElement.appendChild(spanElement);

        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDocument, document);

        assertNull(document.getDoctype());
        assertNull(document.getNamespaceURI());
        assertEquals("https://example.com", document.getDocumentURI());

        Element htmlElement = document.getDocumentElement();
        assertEquals("html", htmlElement.getTagName());

        Element divElementOut = (Element) htmlElement.getFirstChild();
        assertEquals("div", divElementOut.getTagName());

        Element spanElementOut = (Element) divElementOut.getFirstChild();
        assertEquals("span", spanElementOut.getTagName());
    }
    
    @Test
    public void testW3CBuilderHead() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(document);

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");

        w3CBuilder.head(sourceElement, 0);

        Element destElement = document.getDocumentElement();
        assertSame(destElement, (Element) destElement.getParentNode());
        assertEquals("div", destElement.getTagName());
    }
    
    @Test
    public void testW3CBuilderTail() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(document);

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");

        w3CBuilder.head(sourceElement, 0);
        w3CBuilder.tail(sourceElement, 0);

        Element destElement = document.getDocumentElement();
        assertSame(destElement, (Element) destElement.getParentNode());
    }
    
    @Test
    public void testCopyAttributes() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(document);

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");
        sourceElement.attr("class", "container");

        Element destElement = document.createElement("div");
        w3CBuilder.copyAttributes(sourceElement, destElement);

        assertEquals("container", destElement.getAttribute("class"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(document);

        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");
        sourceElement.attr("xmlns", "http://www.w3.org/1999/xhtml");
        sourceElement.attr("xmlns:prefix", "urn:namespace");

        String prefix = w3CBuilder.updateNamespaces(sourceElement);

        assertEquals("prefix", prefix);
        assertEquals("http://www.w3.org/1999/xhtml", w3CBuilder.namespaces.get(""));
        assertEquals("urn:namespace", w3CBuilder.namespaces.get("prefix"));
    }
    
    @Test
    public void testAsString() {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        W3CDom w3CDom = new W3CDom();
        String result = w3CDom.asString(document);

        assertEquals("", result);
    }
    
    // Helper method to create DocumentBuilder with namespace-aware factory
    private DocumentBuilder createDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
    
    // Custom implementation of StringUtil to avoid dependency on external class
    private static class StringUtil {
        public boolean isBlank(String in) {
            if (in == null || in.isEmpty()) {
            	return true;
            }
            
            for (char c : in.toCharArray()) {
            	if (!Character.isWhitespace(c)) {
            		return false;
            	}
            }
            
            return true;
        }
    }
}