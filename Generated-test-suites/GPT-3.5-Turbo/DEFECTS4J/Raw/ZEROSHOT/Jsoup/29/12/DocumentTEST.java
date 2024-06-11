package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        
        assertNotNull("Head element should not be null", doc.head());
        assertNotNull("Body element should not be null", doc.body());
        
        assertEquals("html", doc.children().get(0).tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        
        assertNull("Head element should be null", head);
        
        doc.appendElement("head");
        head = doc.head();
        
        assertNotNull("Head element should not be null", head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        
        assertNull("Body element should be null", body);
        
        doc.appendElement("body");
        body = doc.body();
        
        assertNotNull("Body element should not be null", body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        String title = doc.title();
        assertEquals("Title should be an empty string", "", title);
        
        doc.title("Test Title");
        title = doc.title();
        
        assertEquals("Title should be 'Test Title'", "Test Title", title);
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        doc.normalise();
        
        Element html = doc.children().get(0);
        Element head = html.children().get(0);
        Element body = html.children().get(1);
        
        assertNotNull("Html element should not be null", html);
        assertNotNull("Head element should not be null", head);
        assertNotNull("Body element should not be null", body);
        
        Element title = head.getElementsByTag("title").first();
        assertNotNull("Title element should not be null", title);
        
        assertEquals("html", html.tagName());
        assertEquals("head", head.tagName());
        assertEquals("body", body.tagName());
        assertEquals("Test Title", title.text());
        
        doc = Document.createShell("http://example.com");
        doc.normalise();
        
        html = doc.children().get(0);
        head = html.children().get(0);
        body = html.children().get(1);
        
        assertNotNull("Html element should not be null", html);
        assertNotNull("Head element should not be null", head);
        assertNotNull("Body element should not be null", body);
        
        assertEquals("html", html.tagName());
        assertEquals("head", head.tagName());
        assertEquals("body", body.tagName());
        
        title = head.getElementsByTag("title").first();
        assertNull("Title element should be null", title);
    }
    
    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        
        String text = "This is a test.";
        doc.text(text);
        
        Element body = doc.body();
        assertNotNull("Body element should not be null", body);
        
        assertEquals(text, body.text());
    }
    
    @Test
    public void testToString() {
        Document doc = new Document("http://example.com");
        
        String html = "<html><head></head><body></body></html>";
        
        assertEquals(html, doc.toString());
    }
}