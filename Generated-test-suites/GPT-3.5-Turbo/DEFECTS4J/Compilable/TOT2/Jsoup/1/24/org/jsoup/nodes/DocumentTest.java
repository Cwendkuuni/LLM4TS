package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadAccessor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
    }

    @Test
    public void testBodyAccessor() {
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
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element el = doc.createElement("a");
        assertNotNull(el);
        assertEquals("a", el.tagName());
        assertEquals("http://example.com", el.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        // Add assertions for normalised state
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals(doc.html(), doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}