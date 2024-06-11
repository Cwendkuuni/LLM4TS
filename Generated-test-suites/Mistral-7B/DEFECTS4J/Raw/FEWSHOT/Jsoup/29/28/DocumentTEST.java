package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertEquals(doc, head.parent());
    }

    @Test
    public void testGetBodyElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(doc, body.parent());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals(baseUri, element.baseUri());
        assertNull(element.parent());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element html = doc.appendElement("html");
        Element head = doc.head();
        head.appendElement("title").text("Test Title");
        Element body = doc.body();
        body.appendText("Test Body");
        body.appendElement("p").text("Test Paragraph");

        doc.normalise();

        assertEquals(2, html.childNodeSize());
        assertEquals("head", html.child(0).tagName());
        assertEquals("body", html.child(1).tagName());

        Element titleEl = html.child(0).child(0);
        assertEquals("title", titleEl.tagName());
        assertEquals("Test Title", titleEl.text());

        assertEquals(3, body.childNodeSize());
        assertEquals("p", body.child(1).tagName());
        assertEquals("Test BodyTest Paragraph", body.text());
    }
}