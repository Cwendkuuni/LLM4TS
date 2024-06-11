package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    private final Document document = Document.createShell("http://example.com");
    
    @Test
    public void testHead() {
        assertNull(document.head());
        
        Element head = document.appendElement("head");
        assertEquals(head, document.head());
        
        Element newHead = document.appendElement("head");
        assertEquals(newHead, document.head());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
        
        Element newBody = document.appendElement("body");
        assertEquals(newBody, document.body());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        
        document.title("New Title");
        assertEquals("New Title", document.title());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        document.normalise();
        
        assertNotNull(document.head());
        assertNotNull(document.body());
        
        Element htmlElement = document.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        assertEquals(htmlElement, document.childNodes.get(0));
    }
    
    @Test
    public void testText() {
        Element body = document.body();
        assertNotNull(body);
        
        document.text("Test Text");
        assertEquals("Test Text", body.text());
        
        document.text("New Text");
        assertEquals("New Text", body.text());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().toString());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().toString());
        
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}