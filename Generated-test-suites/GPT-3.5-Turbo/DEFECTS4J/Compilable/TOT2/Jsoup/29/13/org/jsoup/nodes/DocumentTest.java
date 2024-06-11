package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc.baseUri());
        assertEquals("#root", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Test Body");
        assertEquals("Test Body", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("https://www.example.com");
        Document clone = doc.clone();
        assertEquals("#root", clone.nodeName());
        assertEquals(doc.baseUri(), clone.baseUri());
    }

    // Add additional test cases for edge cases and exceptions as needed

}