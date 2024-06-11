package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
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

        // Test setting title
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        // Test removing title
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        assertEquals("<html><head></head><body></body></html>", doc.normalise().outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
}