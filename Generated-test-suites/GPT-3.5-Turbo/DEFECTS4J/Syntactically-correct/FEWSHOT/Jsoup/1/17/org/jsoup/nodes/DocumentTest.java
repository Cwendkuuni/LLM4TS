package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
        doc.appendElement("title").text("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
        doc.appendElement("head").appendElement("title").text("Old Title");
        doc.title("New Title");
        title = doc.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html");
        doc.normalise();
        assertFalse(doc.select("html").isEmpty());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        doc.normalise();
        assertEquals(2, doc.select("html").size());
        doc.select("head").remove();
        doc.normalise();
        assertNotNull(doc.head());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html");
        doc.appendElement("head");
        doc.appendElement("body");
        String outerHtml = doc.outerHtml();
        assertEquals("<html><head></head><body></body></html>", outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body").appendText("test text");
        String text = doc.text();
        assertEquals("test text", text);
        doc.text("new text");
        text = doc.text();
        assertEquals("new text", text);
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }
}