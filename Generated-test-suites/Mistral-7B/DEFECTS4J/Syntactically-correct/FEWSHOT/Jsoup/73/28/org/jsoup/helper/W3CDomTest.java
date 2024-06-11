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
import org.w3c.dom.ProcessingInstruction;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parseFromString("<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>", "http://example.com");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parseFromString("<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>", "http://example.com");
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
        assertSame(w3cDoc.getDocumentElement(), w3cDoc.getElementsByTagName("html").item(0));
        assertEquals(1, w3cDoc.getElementsByTagName("html").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("head").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("title").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("body").getLength());
        assertEquals(1, w3cDoc.getElementsByTagName("p").getLength());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.parser.Parser.parseFromString("<html xmlns:foo='bar' xmlns='baz'><head><title>Test</title></head><body><p xmlns:foo='bar'>Hello world!</p><!-- comment --></body></html>", "http://example.com");
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element html = w3cDoc.getDocumentElement();
        assertSame(html, w3cDoc.getElementsByTagName("html").item(0));
        assertEquals("html", html.getTagName());
        assertEquals("baz", html.getNamespaceURI());

        Element head = (Element) html.getElementsByTagName("head").item(0);
        assertEquals("head", head.getTagName());
        assertNull(head.getNamespaceURI());

        Element title = (Element) head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertNull(title.getNamespaceURI());

        Element body = (Element) html.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());
        assertNull(body.getNamespaceURI());

        Element p = (Element) body.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("bar", p.getNamespaceURI());

        Comment comment = (Comment) body.getElementsByTagName("!--").item(0);
        assertEquals("!--", comment.getNodeName());
        assertNull(comment.getNamespaceURI());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        Element html = w3cDoc.createElement("html");
        Element head = w3cDoc.createElement("head");
        Element title = w3cDoc.createElement("title");
        title.setTextContent("Test");
        head.appendChild(title);
        html.appendChild(head);
        Element body = w3cDoc.createElement("body");
        Element p = w3cDoc.createElement("p");
        p.setTextContent("Hello world!");
        body.appendChild(p);
        html.appendChild(body);
        w3cDoc.appendChild(html);

        String w3cDocAsString = new W3CDom().asString(w3cDoc);
        String expected = "<html><head><title>Test</title></head><body><p>Hello world!</p></body></html>";
        assertEquals(expected, w3cDocAsString);
    }
}