
package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        
        // Check if the document is created properly
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        
        // Check if the head element is returned correctly
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        
        // Check if the body element is returned correctly
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        
        // Check if the title is set and returned correctly
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        
        // Check if the empty string is returned when no title element is present
        doc.select("head").first().remove();
        assertEquals("", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        
        // Check if a new element is created with the given tag name
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        
        // Check if the document is normalised properly
        doc.normalise();
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
        
        // Check if the text content not in the body element is moved to the body element
        doc.appendElement("div").text("Test");
        doc.normalise();
        assertEquals("<html><head></head><body> Test</body></html>", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        
        // Check if the body text is set and returned correctly
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
}
