package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertSame(Tag.valueOf("#root"), doc.tag());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertSame(Tag.valueOf("#root"), doc.tag());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertEquals(doc, head.parent());
        assertNull(head.text());
    }

    @Test
    public void testBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertEquals(doc, body.parent());
        assertNull(body.text());
    }

    @Test
    public void testTitleElement() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        Element titleEl = doc.getElementsByTag("title").first();
        assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element a = doc.createElement("a");
        assertEquals("a", a.tagName());
        assertEquals("http://example.com", a.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("html").appendElement("head");
        head.appendText("Test Head Content");
        Element body = doc.appendElement("html").appendElement("body");
        body.appendText("Test Body Content 1");
        body.appendText("Test Body Content 2");
        doc.normalise();
        assertEquals("Test Body Content 1Test Body Content 2", body.text());
        assertEquals("Test Head Content", head.text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("html").appendElement("head");
        head.appendElement("title").text("Test Title");
        Element body = doc.appendElement("html").appendElement("body");
        body.appendText("Test Body Content");
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body Content</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextNode() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("html").appendElement("body");
        TextNode textNode = body.textNode();
        textNode.text("Test Text");
        assertEquals("Test Text", textNode.text());
    }
}