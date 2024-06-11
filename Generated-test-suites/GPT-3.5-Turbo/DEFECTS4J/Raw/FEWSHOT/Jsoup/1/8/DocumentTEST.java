package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");

        assertNull(doc.head());

        Element head = doc.appendElement("head");
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");

        assertNull(doc.body());

        Element body = doc.appendElement("body");
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");

        assertEquals("", doc.title());

        String title = "Example Page";
        doc.title(title);
        assertEquals(title, doc.title());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");

        doc.text("Hello World");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        assertEquals(0, doc.body().childNodeSize());

        doc.appendElement("div").text("Some text");
        doc.normalise();
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Some text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");

        doc.appendElement("div").text("Some text");
        assertEquals("<div>Some text</div>", doc.outerHtml());
    }
}