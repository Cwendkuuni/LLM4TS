
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://www.example.com");
        
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("https://www.example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("https://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("https://www.example.com");
        
        Element headElement = document.head();
        
        assertNotNull(headElement);
        assertEquals("head", headElement.tagName());
    }
    
    @Test
    public void testBody() {
        Document document = new Document("https://www.example.com");
        
        Element bodyElement = document.body();
        
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("https://www.example.com");
        
        String title = document.title();
        
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        Document document = new Document("https://www.example.com");
        
        document.title("Example Title");
        
        String title = document.title();
        
        assertEquals("Example Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Document document = new Document("https://www.example.com");
        
        Element element = document.createElement("div");
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document document = new Document("https://www.example.com");
        
        document.normalise();
        
        Element htmlElement = document.select("html").first();
        assertEquals(1, htmlElement.children().size());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    @Test
    public void testOuterHtml() {
        Document document = new Document("https://www.example.com");
        
        String outerHtml = document.outerHtml();
        
        assertEquals("", outerHtml);
    }
    
    @Test
    public void testText() {
        Document document = new Document("https://www.example.com");
        
        document.text("Example Text");
        
        Element bodyElement = document.body();
        assertEquals(1, bodyElement.children().size());
        assertEquals("Example Text", bodyElement.text());
    }
    
    @Test
    public void testNodeName() {
        Document document = new Document("https://www.example.com");
        
        String nodeName = document.nodeName();
        
        assertEquals("#document", nodeName);
    }
}
