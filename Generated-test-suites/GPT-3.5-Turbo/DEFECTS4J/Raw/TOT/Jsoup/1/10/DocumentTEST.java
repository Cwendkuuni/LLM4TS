package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitleWithEmptyTitleElement() {
        String title = document.title();
        
        assertNotNull(title);
        assertEquals("", title);
    }
    
    @Test
    public void testTitleWithNonEmptyTitleElement() {
        document.title("Test Title");
        String title = document.title();
        
        assertNotNull(title);
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testTitleWithNoTitleElement() {
        document.head().removeChild(document.head().getElementsByTag("title").first());
        String title = document.title();
        
        assertNotNull(title);
        assertEquals("", title);
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("test");
        
        assertNotNull(element);
        assertEquals("test", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormaliseWithNoHtmlElement() {
        document.select("html").remove();
        document = document.normalise();
        
        assertTrue(document.select("html").size() == 1);
    }
    
    @Test
    public void testNormaliseWithNoHeadElement() {
        document.head().remove();
        document = document.normalise();
        
        assertTrue(document.head() != null);
    }
    
    @Test
    public void testNormaliseWithNoBodyElement() {
        document.select("body").remove();
        document = document.normalise();
        
        assertTrue(document.body() != null);
    }
    
    @Test
    public void testText() {
        String text = "Test Text";
        document.text(text);
        
        assertEquals(text, document.body().text());
    }
    
    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        
        assertNotNull(nodeName);
        assertEquals("#document", nodeName);
    }
}