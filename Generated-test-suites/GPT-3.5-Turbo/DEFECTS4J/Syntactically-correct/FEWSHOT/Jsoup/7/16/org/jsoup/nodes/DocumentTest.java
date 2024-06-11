package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = doc.head();
        assertEquals(html, head.parent());
        assertNotNull(head.getElementsByTag("head").first());
        
        Element body = doc.body();
        assertEquals(html, body.parent());
        assertNotNull(body.getElementsByTag("body").first());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        
        Element head = doc.head();
        assertNull(head);
        
        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        
        Element body = doc.body();
        assertNull(body);
        
        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        
        String title = doc.title();
        assertEquals("", title);
        
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
        
        Element titleElement = doc.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        
        Element div1 = body.appendElement("div");
        Element div2 = body.appendElement("div");
        
        // Add some text nodes outside of body
        doc.prependChild(new TextNode("Text outside body 1", ""));
        doc.appendChild(new TextNode("Text outside body 2", ""));
        
        doc.normalise();
        
        assertEquals("html", doc.child(0).tagName());
        assertEquals("head", html.child(0).tagName());
        assertEquals("body", html.child(1).tagName());
        
        Element normalizedBody = html.child(1);
        assertEquals(4, normalizedBody.children().size());
        
        Element normalizedDiv1 = normalizedBody.child(0);
        Element normalizedDiv2 = normalizedBody.child(1);
        assertEquals("div", normalizedDiv1.tagName());
        assertEquals("div", normalizedDiv2.tagName());
        
        // Check text nodes are moved inside body
        assertEquals("Text outside body 1", normalizedBody.child(2).text());
        assertEquals("Text outside body 2", normalizedBody.child(3).text());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        
        Element div = body.appendElement("div");
        div.appendText("Some text");
        
        String outerHtml = div.outerHtml();
        assertEquals("<div>Some text</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        
        Element body = doc.createElement("body");
        doc.appendChild(body);
        
        doc.text("Some text");
        
        assertEquals("Some text", body.text());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        outputSettings.charset("ISO-8859-1");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);
        
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());
        assertEquals("ISO-8859-1", outputSettings.charset().name());
        assertFalse(outputSettings.prettyPrint());
        assertEquals(2, outputSettings.indentAmount());
    }
}