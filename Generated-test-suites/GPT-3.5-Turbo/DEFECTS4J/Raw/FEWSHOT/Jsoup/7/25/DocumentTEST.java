package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals(html, body.parent());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNull(head);
        
        Element html = doc.createElement("html");
        doc.appendChild(html);
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNull(body);
        
        Element html = doc.createElement("html");
        doc.appendChild(html);
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        String title = doc.title();
        assertEquals("", title);
        
        Element titleEl = doc.createElement("title");
        titleEl.text("Example Page");
        Element head = doc.head();
        head.appendChild(titleEl);
        
        title = doc.title();
        assertEquals("Example Page", title);
        
        doc.title("New Title");
        title = doc.title();
        assertEquals("New Title", title);
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        
        Element html = doc.createElement("html");
        doc.appendChild(html);
        
        doc.normalise();
        
        Element head = doc.head();
        assertNotNull(head);
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element headParent = head.parent();
        assertEquals(html, headParent);
        
        Element bodyParent = body.parent();
        assertEquals(html, bodyParent);
    }
}