package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*; // for assertions

public class DocumentTest {

    @Test
    public void testEmptyDocumentCreation() {
        // Create an empty document
        Document document = new Document("http://example.com");
        // Basic checks
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertNull(document.title());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        // Create a shell document
        Document document = Document.createShell("http://example.com");
        // Basic checks
        assertNotNull(document);
        assertEquals("#html", document.tagName());
        Element html = document.head();
        assertEquals("#head", html.tagName());
        assertEquals("#body", document.body().tagName());
    }

    // Add test cases for head(), title(), body(), createElement(), normalise(), and other methods here

    @Test
    public void testExceptionHandling() {
        try {
            new Document(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new Document("").title("Test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Add test cases for other exception handling scenarios here
    }
}