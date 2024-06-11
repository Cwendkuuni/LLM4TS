package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("", doc.text());
        assertFalse(doc.hasParent());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("", doc.text());
        assertFalse(doc.hasParent());
        
        Element html = doc.children().get(0);
        assertNotNull(html);
        assertEquals("html", html.tagName());
        assertTrue(html.hasParent());
        
        Element head = html.children().get(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertTrue(head.hasParent());
        
        Element body = html.children().get(1);
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertTrue(body.hasParent());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.head());
        
        Element head = new Element(Tag.valueOf("head"), "");
        doc.appendChild(head);
        
        assertEquals(head, doc.head());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.body());
        
        Element body = new Element(Tag.valueOf("body"), "");
        doc.appendChild(body);
        
        assertEquals(body, doc.body());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        assertEquals("", doc.title());
        
        doc.title("Page Title");
        
        assertEquals("Page Title", doc.title());
        
        doc.title("New Title");
        
        assertEquals("New Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");

        Element element = doc.createElement("a");

        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertNull(element.parent());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        
        assertEquals(doc, doc.normalise());
    }
    
    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        
        Element body = new Element(Tag.valueOf("body"), "");
        doc.appendChild(body);
        
        TextNode textNode1 = new TextNode("text1", "");
        body.appendChild(textNode1);
        
        TextNode textNode2 = new TextNode("text2", "");
        body.appendChild(textNode2);
        
        doc.normaliseTextNodes(body);
        
        assertEquals("text1 text2", body.text());
        assertEquals(2, body.childNodes().size());
        assertNull(body.previousSibling());
        assertNull(body.nextSibling());
        
        assertEquals(body, body.childNodes().get(0).parent());
        assertEquals(body, body.childNodes().get(1).parent());
    }
    
    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        
        Element html = new Element(Tag.valueOf("html"), "");
        doc.appendChild(html);
        
        Element head = new Element(Tag.valueOf("head"), "");
        html.appendChild(head);
        
        Element body = new Element(Tag.valueOf("body"), "");
        html.appendChild(body);
        
        Element h1 = new Element(Tag.valueOf("h1"), "");
        head.appendChild(h1);
        
        Element h2 = new Element(Tag.valueOf("h2"), "");
        head.appendChild(h2);
        
        Element h3 = new Element(Tag.valueOf("h3"), "");
        body.appendChild(h3);
        
        Element foundElement1 = doc.findFirstElementByTagName("h1", doc);
        assertNotNull(foundElement1);
        assertEquals(h1, foundElement1);
        
        Element foundElement2 = doc.findFirstElementByTagName("h2", doc);
        assertNotNull(foundElement2);
        assertEquals(h2, foundElement2);
        
        Element foundElement3 = doc.findFirstElementByTagName("h3", doc);
        assertNotNull(foundElement3);
        assertEquals(h3, foundElement3);
        
        Element foundElement4 = doc.findFirstElementByTagName("h4", doc);
        assertNull(foundElement4);
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");

        Element html = new Element(Tag.valueOf("html"), "");
        doc.appendChild(html);

        Element head = new Element(Tag.valueOf("head"), "");
        html.appendChild(head);

        Element body = new Element(Tag.valueOf("body"), "");
        html.appendChild(body);

        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        
        Element body = new Element(Tag.valueOf("body"), "");
        doc.appendChild(body);
        
        doc.text("Some text");
        
        assertEquals("Some text", doc.text());
        assertEquals("Some text", body.text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        
        assertEquals("#document", doc.nodeName());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        
        assertNotNull(doc.outputSettings());
        
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        
        outputSettings.charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}