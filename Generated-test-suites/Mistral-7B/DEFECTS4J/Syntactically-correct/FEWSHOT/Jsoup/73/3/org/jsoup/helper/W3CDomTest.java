package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSOutput;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>foo</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertNotNull(w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>foo</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);
        Element rootEl = (Element) w3cDoc.getDocumentElement().getElementsByTagName("html").item(0);
        assertEquals(1, rootEl.getChildNodes().getLength());
        Element bodyEl = (Element) rootEl.getChildNodes().item(0);
        assertEquals(1, bodyEl.getChildNodes().getLength());
        Element pEl = (Element) bodyEl.getChildNodes().item(0);
        assertEquals("p", pEl.getTagName());
        assertEquals("foo", pEl.getTextContent());
    }

    @Test
    public void testAsString() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootEl = doc.createElement("html");
        Element bodyEl = doc.createElement("body");
        Element pEl = doc.createElement("p");
        pEl.setTextContent("foo");
        rootEl.appendChild(bodyEl);
        bodyEl.appendChild(pEl);
        doc.appendChild(rootEl);

        String w3cString = new W3CDom().asString(doc);
        String xmlString = "<html><body><p>foo</p></body></html>";
        assertEquals(xmlString, w3cString);
    }

    @Test
    public void testW3CBuilder() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootEl = doc.createElement("html");
        Element bodyEl = doc.createElement("body");
        Element pEl1 = doc.createElement("p");
        Element pEl2 = doc.createElement("p");
        pEl1.setTextContent("foo");
        pEl2.setTextContent("bar");
        rootEl.appendChild(bodyEl);
        bodyEl.appendChild(pEl1);
        bodyEl.appendChild(pEl2);
        doc.appendChild(rootEl);

        String xmlnsPrefix = "xmlns:x";
        String xmlnsValue = "http://example.com";
        Element rootEl2 = doc.createElement("html");
        rootEl2.setAttribute(xmlnsPrefix + ":x", xmlnsValue);
        Element bodyEl2 = doc.createElement("body");
        rootEl2.setAttribute("xmlns", xmlnsValue);
        Element pEl3 = doc.createElement("p");
        pEl3.setTextContent("baz");
        rootEl2.appendChild(bodyEl2);
        bodyEl2.appendChild(pEl3);
        doc.appendChild(rootEl2);

        Document w3cDoc = new W3CDom().fromJsoup(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        NodeTraversor.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof Element) {
                    Element el = (Element) node;
                    assertEquals("html", el.getTagName());
                    String prefix = "";
                    if (el.getAttributes().getNamedItem("xmlns") != null) {
                        prefix = "xmlns:";
                    }
                    String namespace = el.getNamespaceURI();
                    assertEquals(namespace, "http://example.com");
                    assertEquals(prefix + "x", el.getPrefix());
                    assertEquals(1, el.getChildNodes().getLength());
                    Element childEl = (Element) el.getChildNodes().item(0);
                    assertEquals("body", childEl.getTagName());
                    assertEquals("", childEl.getPrefix());
                    assertEquals("", childEl.getNamespaceURI());
                    assertEquals(2, childEl.getChildNodes().getLength());
                    Element grandChildEl1 = (Element) childEl.getChildNodes().item(0);
                    assertEquals("p", grandChildEl1.getTagName());
                    assertEquals("", grandChildEl1.getPrefix());
                    assertEquals("", grandChildEl1.getNamespaceURI());
                    assertEquals("foo", grandChildEl1.getTextContent());
                    Element grandChildEl2 = (Element) childEl.getChildNodes().item(1);
                    assertEquals("p", grandChildEl2.getTagName());
                    assertEquals("", grandChildEl2.getPrefix());
                    assertEquals("", grandChildEl2.getNamespaceURI());
                    assertEquals("bar", grandChildEl2.getTextContent());
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // Empty
            }
        }, rootEl2);
    }

    @Test
    public void testWithNamespaces() {
        String xmlnsValue = "http://example.com";
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootEl = doc.createElement("html");
        rootEl.setAttribute("xmlns:x", xmlnsValue);
        rootEl.setAttribute("xmlns", xmlnsValue);
        Element bodyEl = doc.createElement("body");
        rootEl.appendChild(bodyEl);
        Element pEl1 = doc.createElement("p:foo");
        pEl1.setTextContent("foo1");
        bodyEl.appendChild(pEl1);
        Element pEl2 = doc.createElement("p:bar");
        pEl2.setTextContent("bar1");
        bodyEl.appendChild(pEl2);

        Document w3cDoc = new W3CDom().fromJsoup(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        NodeVisitor nodeVisitor = new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof Element) {
                    Element el = (Element) node;
                    assertEquals("html", el.getTagName());
                    assertEquals("http://example.com", el.getNamespaceURI());
                    assertEquals("xmlns:x", el.getAttributes().getNamedItem("xmlns:x").getNodeValue());
                    assertEquals("xmlns", el.getAttributes().getNamedItem("xmlns").getNodeValue());
                    assertEquals(1, el.getChildNodes().getLength());
                    Element childEl = (Element) el.getChildNodes().item(0);
                    assertEquals("body", childEl.getTagName());
                    assertEquals("http://example.com", childEl.getNamespaceURI());
                    assertEquals("", childEl.getPrefix());
                    assertEquals(2, childEl.getChildNodes().getLength());
                    Element grandChildEl1 = (Element) childEl.getChildNodes().item(0);
                    assertEquals("p", grandChildEl1.getTagName());
                    assertEquals("http://example.com", grandChildEl1.getNamespaceURI());
                    assertEquals("foo", grandChildEl1.getLocalName());
                    assertEquals("foo:p", grandChildEl1.getTagName());
                    assertEquals("foo", grandChildEl1.getPrefix());
                    assertEquals("http://example.com", grandChildEl1.getNamespaceURI());
                    assertEquals("foo1", grandChildEl1.getTextContent());
                    Element grandChildEl2 = (Element) childEl.getChildNodes().item(1);
                    assertEquals("p", grandChildEl2.getTagName());
                    assertEquals("http://example.com", grandChildEl2.getNamespaceURI());
                    assertEquals("bar", grandChildEl2.getLocalName());
                    assertEquals("bar:p", grandChildEl2.getTagName());
                    assertEquals("bar", grandChildEl2.getPrefix());
                    assertEquals("http://example.com", grandChildEl2.getNamespaceURI());
                    assertEquals("bar1", grandChildEl2.getTextContent());
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // Empty
            }
        };
        NodeTraversor.traverse(nodeVisitor, w3cDoc.getDocumentElement());
    }
}