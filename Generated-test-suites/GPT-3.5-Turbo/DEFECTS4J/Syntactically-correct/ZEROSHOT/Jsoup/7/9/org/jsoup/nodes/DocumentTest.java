package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        // Given
        String baseUri = "http://example.com";
        
        // When
        Document document = new Document(baseUri);
        
        // Then
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        // Given
        String baseUri = "http://example.com";
        
        // When
        Document document = Document.createShell(baseUri);
        
        // Then
        Assert.assertEquals("html", document.child(0).nodeName());
        Assert.assertEquals("head", document.head().nodeName());
        Assert.assertEquals("body", document.body().nodeName());
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testHead() {
        // Given
        Document document = new Document("http://example.com");
        Element headElement = document.createElement("head");
        document.appendChild(headElement);
        
        // When
        Element head = document.head();
        
        // Then
        Assert.assertEquals(headElement, head);
    }
    
    @Test
    public void testBody() {
        // Given
        Document document = new Document("http://example.com");
        Element bodyElement = document.createElement("body");
        document.appendChild(bodyElement);
        
        // When
        Element body = document.body();
        
        // Then
        Assert.assertEquals(bodyElement, body);
    }
    
    @Test
    public void testTitle() {
        // Given
        Document document = new Document("http://example.com");
        document.head().appendElement("title").text("Test Title");
        
        // When
        String title = document.title();
        
        // Then
        Assert.assertEquals("Test Title", title);
    }
    
    @Test
    public void testSetTitle() {
        // Given
        Document document = new Document("http://example.com");
        
        // When
        document.title("Test Title");
        
        // Then
        Element titleElement = document.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title", titleElement.text());
    }
    
    @Test
    public void testCreateElement() {
        // Given
        Document document = new Document("http://example.com");
        
        // When
        Element element = document.createElement("div");
        
        // Then
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        // Given
        Document document = new Document("http://example.com");
        Element htmlElement = document.createElement("html");
        Element headElement = document.createElement("head");
        Element bodyElement = document.createElement("body");
        TextNode textNode = new TextNode("Test", "");
        document.appendChild(htmlElement);
        htmlElement.appendChild(headElement);
        htmlElement.appendChild(bodyElement);
        document.appendChild(textNode);
        
        // When
        document.normalise();
        
        // Then
        Assert.assertEquals("html", document.child(0).nodeName());
        Assert.assertEquals("head", document.head().nodeName());
        Assert.assertEquals("body", document.body().nodeName());
        Assert.assertEquals(textNode, document.body().child(1));
    }
    
    @Test
    public void testOuterHtml() {
        // Given
        Document document = new Document("http://example.com");
        document.createElement("div").text("Test");
        
        // When
        String outerHtml = document.outerHtml();
        
        // Then
        Assert.assertEquals("<div>Test</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        // Given
        Document document = new Document("http://example.com");
        
        // When
        document.text("Test");
        
        // Then
        Assert.assertEquals("Test", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        // Given
        Document document = new Document("http://example.com");
        
        // When
        String nodeName = document.nodeName();
        
        // Then
        Assert.assertEquals("#document", nodeName);
    }
    
    @Test
    public void testOutputSettings() {
        // Given
        Document document = new Document("http://example.com");
        
        // When
        Document.OutputSettings outputSettings = document.outputSettings();
        
        // Then
        Assert.assertNotNull(outputSettings);
    }

}