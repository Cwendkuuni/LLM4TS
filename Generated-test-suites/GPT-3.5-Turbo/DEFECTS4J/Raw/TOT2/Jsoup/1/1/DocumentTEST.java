package org.jsoup.nodes;

import static org.junit.Assert.*;
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
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        assertThrows(IllegalArgumentException.class, () -> {
            doc.title(null);
        });
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        Element el = doc.createElement("div");
        assertNotNull(el);
        assertEquals("div", el.tagName());
        assertEquals("http://example.com", el.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendText("text node");
        doc.normalise();
        assertEquals("text node", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Sample Text");
        assertEquals("Sample Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}