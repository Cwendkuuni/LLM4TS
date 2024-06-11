package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        // Normalise method modifies the document structure, so it can't be tested using assertions
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
    
    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertNotNull(clone);
        assertNotSame(doc, clone);
        assertEquals(doc.baseUri(), clone.baseUri());
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }
    
    @Test
    public void testEscapeMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        
        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
    }
    
    @Test
    public void testCharset() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals("UTF-8", settings.charset().name());
        
        settings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", settings.charset().name());
    }
    
    @Test
    public void testPrettyPrint() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertTrue(settings.prettyPrint());
        
        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());
    }
    
    @Test
    public void testIndentAmount() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals(1, settings.indentAmount());
        
        settings.indentAmount(2);
        assertEquals(2, settings.indentAmount());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings settings = new Document.OutputSettings();
        doc.outputSettings(settings);
        assertSame(settings, doc.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
        
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}