package org.jsoup.nodes;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DocumentTest {
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        doc.title("Example Title");
        String title = doc.title();
        assertEquals("Example Title", title);
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        doc.createElement("p");
        doc.createElement("div");
        doc.createElement("span");
        doc.normalise();
        Element html = doc.head().parent();
        assertEquals("html", html.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.text("Example Text");
        assertEquals("Example Text", doc.body().text());
    }
    
    @Test
    public void testClone() {
        Document doc = new Document("http://www.example.com");
        Document clone = doc.clone();
        assertEquals(doc.baseUri(), clone.baseUri());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://www.example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
        doc.quirksMode(quirksMode);
        assertEquals(quirksMode, doc.quirksMode());
    }
}