package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
    private Document doc;

    @Test
    public void testCreateShell() {
        doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testHead() {
        doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Title");
        title = doc.title();
        assertEquals("Title", title);

        doc.appendElement("head")
            .appendElement("title")
            .text("New Title");
        title = doc.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testNormalise() {
        doc = new Document("http://example.com");
        doc.appendElement("html");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.select("html").size());
        assertEquals(1, doc.select("head").size());
        assertEquals(1, doc.select("body").size());
    }

    @Test
    public void testCreateElement() {
        doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        doc = new Document("http://example.com");
        doc.appendElement("html");
        String outerHtml = doc.outerHtml();
        assertEquals("<html></html>", outerHtml);
    }

    @Test
    public void testText() {
        doc = new Document("http://example.com");
        doc.appendElement("body");
        doc.text("Hello");
        Element body = doc.body();
        assertEquals("Hello", body.text());
    }

    @Test
    public void testNodeName() {
        doc = new Document("http://example.com");
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }
}