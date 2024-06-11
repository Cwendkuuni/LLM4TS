package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    // Test constructor
    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }
    
    // Test createShell method
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        
        assertNotNull(doc);
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }
    
    // Test head method
    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }
    
    // Test body method
    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }
    
    // Test title method when title element is present
    @Test
    public void testTitleWithTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Example Title");
        
        String title = doc.title();
        
        assertEquals("Example Title", title);
    }
    
    // Test title method when title element is not present
    @Test
    public void testTitleWithoutTitleElement() {
        Document doc = Document.createShell("http://example.com");
        
        String title = doc.title();
        
        assertEquals("", title);
    }
    
    // Test title method when title element is present but empty
    @Test
    public void testTitleWithEmptyTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title");
        
        String title = doc.title();
        
        assertEquals("", title);
    }
    
    // Test title method when multiple title elements are present
    @Test
    public void testTitleWithMultipleTitleElements() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Title 1");
        doc.head().appendElement("title").text("Title 2");
        doc.head().appendElement("title").text("Title 3");
        
        String title = doc.title();
        
        assertEquals("Title 1", title);
    }
    
    // Test title method when title element contains leading or trailing whitespace
    @Test
    public void testTitleWithWhitespaceTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("  Example Title  ");
        
        String title = doc.title();
        
        assertEquals("Example Title", title);
    }
    
    // Test setTitle method when title element is not present
    @Test
    public void testSetTitleWithoutTitleElement() {
        Document doc = Document.createShell("http://example.com");
        
        doc.title("Example Title");
        Element titleEl = doc.getElementsByTag("title").first();
        
        assertNotNull(titleEl);
        assertEquals("Example Title", titleEl.text());
    }
    
    // Test setTitle method when title element is present
    @Test
    public void testSetTitleWithTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Previous Title");
        
        doc.title("New Title");
        Element titleEl = doc.getElementsByTag("title").first();
        
        assertNotNull(titleEl);
        assertEquals("New Title", titleEl.text());
    }
    
    // Test setTitle method when multiple title elements are present
    @Test
    public void testSetTitleWithMultipleTitleElements() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Title 1");
        doc.head().appendElement("title").text("Title 2");
        doc.head().appendElement("title").text("Title 3");
        
        doc.title("New Title");
        Element titleEl = doc.getElementsByTag("title").first();
        
        assertNotNull(titleEl);
        assertEquals("New Title", titleEl.text());
    }
    
    // Test createElement method
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("a");
        
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    // Test normalise method
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Example Title");
        doc.appendElement("div");
        doc.appendElement("p").text("Example Paragraph");
        
        doc.normalise();
        
        Element htmlEl = doc.getElementsByTag("html").first();
        Element head = doc.head();
        Element body = doc.body();
        Element titleEl = doc.getElementsByTag("title").first();
        
        assertNotNull(htmlEl);
        assertNotNull(head);
        assertNotNull(body);
        assertNotNull(titleEl);
        assertNull(doc.getElementsByTag("div").first());
        assertNull(doc.getElementsByTag("p").first());
        
        assertEquals("head", head.parent().nodeName());
        assertEquals("html", head.parent().parent().nodeName());
        
        assertEquals("body", body.parent().nodeName());
        assertEquals("html", body.parent().parent().nodeName());
        
        assertEquals("title", titleEl.parent().nodeName());
        assertEquals("head", titleEl.parent().parent().nodeName());
    }
    
    // Test normalise method when html element is not present
    @Test
    public void testNormaliseWithoutHtmlElement() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Example Title");
        doc.appendElement("div");
        doc.appendElement("p").text("Example Paragraph");
        
        doc.normalise();
        
        Element htmlEl = doc.getElementsByTag("html").first();
        Element head = doc.head();
        Element body = doc.body();
        Element titleEl = doc.getElementsByTag("title").first();
        
        assertNotNull(htmlEl);
        assertNotNull(head);
        assertNotNull(body);
        assertNotNull(titleEl);
        assertNull(doc.getElementsByTag("div").first());
        assertNull(doc.getElementsByTag("p").first());
        
        assertEquals("head", head.parent().nodeName());
        assertEquals("html", head.parent().parent().nodeName());
        
        assertEquals("body", body.parent().nodeName());
        assertEquals("html", body.parent().parent().nodeName());
        
        assertEquals("title", titleEl.parent().nodeName());
        assertEquals("head", titleEl.parent().parent().nodeName());
    }
    
    // Test outerHtml method
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.text("Example Text");
        
        String outerHtml = doc.outerHtml();
        
        assertEquals("Example Text", outerHtml);
    }
    
    // Test text method
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Example Text");
        
        assertEquals("Example Text", doc.html());
    }
    
    // Test nodeName method
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        
        assertEquals("#document", doc.nodeName());
    }
    
    // Test outputSettings method
    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        
        assertNotNull(settings);
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), settings.charset());
        assertTrue(settings.prettyPrint());
        assertEquals(1, settings.indentAmount());
    }
}