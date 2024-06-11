package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals("html", document.nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element headElement = document.head();
        assertNull(headElement);
        
        Element head = new Element(Tag.valueOf("head"), "");
        document.appendChild(head);
        headElement = document.head();
        assertEquals(head, headElement);
    }
    
    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        assertNull(bodyElement);
        
        Element body = new Element(Tag.valueOf("body"), "");
        document.appendChild(body);
        bodyElement = document.body();
        assertEquals(body, bodyElement);
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);
        
        Element titleElement = new Element(Tag.valueOf("title"), "");
        document.head().appendChild(titleElement);
        titleElement.text("Test Title");
        
        title = document.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        Element titleElement = new Element(Tag.valueOf("title"), "");
        document.head().appendChild(titleElement);
        
        document.title("New Title");
        assertEquals("New Title", titleElement.text());
    }
    
    @Test
    public void testNormalize() {
        Document document = new Document("http://example.com");
        
        Element htmlElement = new Element(Tag.valueOf("html"), "");
        document.appendChild(htmlElement);
        
        Element headElement = new Element(Tag.valueOf("head"), "");
        Element bodyElement = new Element(Tag.valueOf("body"), "");
        htmlElement.appendChild(headElement);
        htmlElement.appendChild(bodyElement);
        
        Element divElement = document.createElement("div");
        headElement.appendChild(divElement);
        
        TextNode textNode1 = new TextNode("Text 1", "");
        TextNode textNode2 = new TextNode("Text 2", "");
        TextNode textNode3 = new TextNode("Text 3", "");
        TextNode textNode4 = new TextNode("Text 4", "");
        
        htmlElement.appendChild(textNode1);
        headElement.appendChild(textNode2);
        bodyElement.appendChild(textNode3);
        divElement.appendChild(textNode4);
        
        document.normalise();
        
        assertEquals("html", document.childNodes().get(0).nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
        assertEquals("Text 2", document.head().text());
        assertEquals("Text 1 Text 3 Text 4", document.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        assertEquals("", document.outerHtml());
    }
    
    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        Element bodyElement = new Element(Tag.valueOf("body"), "");
        document.appendChild(bodyElement);
        
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }
}