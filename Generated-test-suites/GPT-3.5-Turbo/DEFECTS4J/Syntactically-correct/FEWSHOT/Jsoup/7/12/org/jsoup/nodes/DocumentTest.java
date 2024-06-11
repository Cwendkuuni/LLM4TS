package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("html", document.nodeName());
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.appendElement("head");
        assertEquals(head, document.head());
    }
    
    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        assertNull(document.title());
        
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        
        // Test normalise method
        document.normalise();
        
        // Check if head and body are present
        assertNotNull(document.head());
        assertNotNull(document.body());
        
        // Check if elements have been moved to the correct locations
        assertEquals(html, document.childNode(0));
        assertEquals(head, html.childNode(0));
        assertEquals(body, html.childNode(1));
        
        // Check if text nodes have been moved to body
        assertEquals(0, head.childNodes.size());
        assertEquals(2, body.childNodes.size());
    }
    
    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");
        assertEquals("<body></body>", document.outerHtml());
        
        body.appendElement("div");
        assertEquals("<body><div></div></body>", document.outerHtml());
    }
    
    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");
        body.appendElement("p").text("Test text");
        assertEquals("<body><p>Test text</p></body>", document.outerHtml());
    }
    
    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}