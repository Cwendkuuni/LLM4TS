
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;
    
    @Before
    public void setUp() {
        document = Document.createShell("https://www.example.com");
    }
    
    @Test
    public void testCreateShell() {
        String baseUri = "https://www.example.com";
        Document doc = Document.createShell(baseUri);
        
        assertEquals("#root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        
        assertEquals("html", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
        
        document.appendElement("head");
        Element head = document.head();
        
        assertEquals("head", head.nodeName());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        
        document.appendElement("body");
        Element body = document.body();
        
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());

        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
        
        document.title("New Title");
        assertEquals("New Title", document.title());
    }
    
    @Test
    public void testText() {
        Element body = document.body();
        body.text("Sample Text");
        
        assertEquals("Sample Text", body.text());
    }
    
    @Test
    public void testNormalize() {
        assertEquals("<html><head></head></html>", document.outerHtml());
        
        document.normalise();
        
        assertEquals("<html><head></head><body></body></html>", document.outerHtml());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }
    
}
