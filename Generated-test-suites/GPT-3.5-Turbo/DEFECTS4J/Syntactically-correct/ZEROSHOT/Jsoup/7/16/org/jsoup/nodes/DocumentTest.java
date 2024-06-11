package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element headElement = document.head();
        
        assertNull(headElement);
    }
    
    @Test
    public void testBody() {
        Element bodyElement = document.body();
        
        assertNull(bodyElement);
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        document.title("Test Title");
        
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        
        assertNotNull(normalizedDocument);
    }
    
    @Test
    public void testcreateElement() {
        Element element = document.createElement("div");
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }
    
    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        
        assertEquals("", outerHtml);
    }
    
    @Test
    public void testTextNode() {
        document.text("Test");
        
        Element bodyElement = document.body();
        
        assertNotNull(bodyElement);
        assertEquals("Test", bodyElement.text());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    }
}