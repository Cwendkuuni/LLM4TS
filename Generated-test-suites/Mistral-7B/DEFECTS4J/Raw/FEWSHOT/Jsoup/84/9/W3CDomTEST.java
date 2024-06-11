package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p id='p1'>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html><head/><body><p id='p1'>Hello World</p></body></html>", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p id='p1'>Hello World</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertNotNull(w3cDocument);
        assertEquals("<html><head/><body><p id='p1'>Hello World</p></body></html>", w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head xmlns:foo='http://example.com/'><body><p id='p1' xmlns:bar='http://example.org/' class='foo bar'>Hello <foo:world xmlns:qux='http://example.net/'/> World</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Node root = w3cDocument.getDocumentElement();
        assertEquals("html", root.getNodeName());

        Element head = (Element) root.getElementsByTagName("head").item(0);
        assertNull(head.getNamespaceURI());
        assertNull(head.getPrefix());

        Element body = (Element) root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getNodeName());
        assertEquals("http://example.com/", body.getNamespaceURI());
        assertEquals("foo", body.getPrefix());

        Element p = (Element) body.getElementsByTagName("p").item(0);
        assertEquals("p", p.getNodeName());
        assertEquals("http://example.com/", p.getNamespaceURI());
        assertEquals("foo", p.getPrefix());
        assertEquals("bar", p.getAttributes().getNamedItem("class").getNodeValue());

        Node worldNode = p.getElementsByTagName("world").item(0);
        assertEquals("world", worldNode.getNodeName());
        assertEquals("http://example.net/", worldNode.getNamespaceURI());
        assertEquals("qux", worldNode.getPrefix());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new DocumentBuilderFactory().newDocumentBuilder().newDocument();
        Element root = w3cDocument.createElement("html");
        root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        Element body = w3cDocument.createElement("body");
        root.appendChild(body);
        Element p = w3cDocument.createElement("p");
        p.setTextNode(w3cDocument.createTextNode("Hello World"));
        body.appendChild(p);
        root.appendChild(p);

        Document w3cDocumentConverted = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>"));

        new W3CDom().convert(w3cDocumentConverted, w3cDocument);

        String xml = new W3CDom().asString(w3cDocument);
        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>Hello World</p></body></html>", xml);
    }
}