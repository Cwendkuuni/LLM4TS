package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
        
        Element head = doc.appendElement("head");
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());
        
        Element body = doc.appendElement("body");
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        
        doc.appendElement("head").appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        
        assertEquals("div", element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        
        doc.normalise();
        
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body").appendText("Hello World");
        
        assertEquals("Hello World", doc.text());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        doc.outputSettings(outputSettings);
        
        assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        
        doc.quirksMode(Document.QuirksMode.noQuirks);
        
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
    }
}