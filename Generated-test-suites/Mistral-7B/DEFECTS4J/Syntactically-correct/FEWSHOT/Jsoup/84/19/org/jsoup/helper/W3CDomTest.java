package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Content</element>\n</root>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertEquals("<root xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><element xmlns=\"\" attr=\"value\">Content</element></root>", asString(w3cDocument));
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p>Hello, world!</p></body></html>", asString(w3cDocument));
    }

    @Test
    public void testAsString() {
        Document document = newDocument();
        Element root = document.createElement("root");
        root.setTextContent("Hello, world!");
        document.appendChild(root);
        Assert.assertEquals("<root xmlns=\"\">Hello, world!</root>", new W3CDom().asString(document));
    }

    @Test
    public void testNamespaces() {
        Document document = newDocument();
        Element root = document.createElement("root");
        root.setAttribute("xmlns:x", "http://example.com/namespace");
        document.appendChild(root);

        Element child = document.createElement("child");
        child.setTextContent("Child element");
        root.appendChild(child);

        Element element = document.createElementNS("http://example.com/namespace", "element");
        element.setTextContent("Namespace element");
        root.appendChild(element);

        assertEquals("<root xmlns:x=\"http://example.com/namespace\"><child>Child element</child><element xmlns=\"http://example.com/namespace\">Namespace element</element></root>", new W3CDom().asString(document));
    }

    private Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    private String asString(Document document) {
        try {
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}