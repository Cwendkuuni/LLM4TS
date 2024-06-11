package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        
        assertEquals("http://www.example.com", doc.baseUri());
        
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        
        assertNull(head);
        
        doc.appendElement("head");
        head = doc.head();
        
        assertNotNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        
        assertNull(body);
        
        doc.appendElement("body");
        body = doc.body();
        
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        
        String title = doc.title();
        
        assertEquals("", title);
        
        doc.appendElement("head").appendElement("title").text("Test Title");
        title = doc.title();
        
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testSetTitle() {
        Document doc = new Document("http://www.example.com");
        
        doc.title("Test Title");
        
        Element titleEl = doc.getElementsByTag("title").first();
        
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        
        Element element = doc.createElement("div");
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://www.example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        
        doc.normalise();
        
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        
        assertEquals(0, html.childNodes.size());
        assertEquals(0, head.childNodes.size());
        assertEquals(0, body.childNodes.size());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        
        String outerHtml = doc.outerHtml();
        
        assertEquals("", outerHtml);
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        
        doc.appendElement("body");
        
        doc.text("Test Text");
        
        Element body = doc.body();
        
        assertNotNull(body);
        assertEquals("Test Text", body.text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://www.example.com");
        
        String nodeName = doc.nodeName();
        
        assertEquals("#document", nodeName);
    }
}