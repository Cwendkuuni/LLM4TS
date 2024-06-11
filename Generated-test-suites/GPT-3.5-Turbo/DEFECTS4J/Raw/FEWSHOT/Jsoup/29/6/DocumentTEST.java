package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Assert that the document is not null
        assertNotNull(document);
        
        // Get the head element and assert that it is not null
        Element head = document.head();
        assertNotNull(head);
        
        // Get the body element and assert that it is not null
        Element body = document.body();
        assertNotNull(body);
        
        // Assert that the base URI of the document is equal to the provided base URI
        assertEquals("https://example.com", document.baseUri());
    }
    
    @Test
    public void testHead() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Get the head element and assert that it is not null
        Element head = document.head();
        assertNotNull(head);
        
        // Assert that the tag name of the head element is "head"
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Get the body element and assert that it is not null
        Element body = document.body();
        assertNotNull(body);
        
        // Assert that the tag name of the body element is "body"
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Set the document title
        document.title("Example Title");
        
        // Get the document title and assert its value
        String title = document.title();
        assertEquals("Example Title", title);
    }
    
    @Test
    public void testTitleNoTitleElement() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Get the document title when no title element is present
        String title = document.title();
        
        // Assert that the title is an empty string
        assertEquals("", title);
    }
    
    @Test
    public void testText() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Set the text of the body element
        document.text("Example Text");
        
        // Get the text of the body element and assert its value
        String text = document.body().text();
        assertEquals("Example Text", text);
    }
    
    @Test
    public void testTextNoBodyElement() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Set the text of the document when no body element is present
        document.text("Example Text");
        
        // Get the text of the document and assert its value
        String text = document.text();
        assertEquals("Example Text", text);
    }
    
    @Test
    public void testClone() {
        // Create a new Document object
        Document document = Document.createShell("https://example.com");
        
        // Clone the document
        Document clone = document.clone();
        
        // Assert that the cloned document is not null
        assertNotNull(clone);
        
        // Assert that the cloned document is not the same object as the original document
        assertNotSame(document, clone);
        
        // Assert that the cloned document has the same base URI as the original document
        assertEquals(document.baseUri(), clone.baseUri());
    }
}