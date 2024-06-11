package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
    
    // Test case for Document constructor
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
    }

    // Test case for creating a shell document
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    // Test case for getting the title of the document
    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    // Test case for creating a new element
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.nodeName());
    }

    // Test case for normalizing the document
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    // Test case for setting the text of the body
    @Test
    public void testSetText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test text");
        assertEquals("Test text", doc.body().text());
    }
    
    // Test case for edge case where title is not set
    @Test
    public void testEmptyTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
    }
    
    // Test case for exception handling when title is null
    @Test(expected = IllegalArgumentException.class)
    public void testNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }
}