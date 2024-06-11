package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        // Test empty title
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element el = doc.createElement("div");
        assertNotNull(el);
        assertEquals("div", el.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        doc.normalise();
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals(doc.outerHtml(), doc.html());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Body");
        assertEquals("Test Body", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
    
}