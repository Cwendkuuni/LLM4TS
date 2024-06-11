package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        
        Element html = doc.select("html").first();
        assertEquals("html", html.tagName());
        
        Element head = doc.select("head").first();
        assertEquals("head", head.tagName());
        
        Element body = doc.select("body").first();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        
        Element head = doc.head();
        assertNull(head);
        
        doc.head().appendElement("title").text("Example Title");
        assertEquals("Example Title", doc.title());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        
        Element body = doc.body();
        assertNull(body);
        
        doc.body().text("Example Body");
        assertEquals("Example Body", doc.body().text());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        
        assertEquals("", doc.title());
        
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());
    }

    @Test
    public void testNormalize() {
        Document doc = new Document("https://www.example.com");
        
        Element head = doc.appendElement("head");
        head.appendText("Example Head");
        
        Element body = doc.appendElement("body");
        body.appendText("Example Body");
        
        doc.appendText("Example Unnormalized Text");
        
        doc.normalise();
        
        assertEquals("Example Head", doc.head().text());
        assertEquals("Example Body Example Unnormalized Text", doc.body().text());
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("https://www.example.com");
        
        Element div = doc.createElement("div");
        assertEquals("div", div.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://www.example.com");
        
        doc.body().text("Example Body");
        
        assertEquals("Example Body", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        
        doc.text("Example Text");
        
        assertEquals("Example Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://www.example.com");
        
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        outputSettings.charset("ISO-8859-1");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);
        
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());
        assertEquals("ISO-8859-1", outputSettings.charset().name());
        assertEquals(false, outputSettings.prettyPrint());
        assertEquals(2, outputSettings.indentAmount());
    }
}