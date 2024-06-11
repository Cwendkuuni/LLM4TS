import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setup() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testConstructor() {
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }
    
    @Test
    public void testTitleWithNoTitleElement() {
        String title = document.title();
        assertEquals("", title);
    }
    
    @Test
    public void testTitleWithExistingTitleElement() {
        Element titleElement = document.createElement("title");
        titleElement.text("Test Title");
        
        Element head = document.head();
        if (head == null) {
            head = document.appendElement("head");
        }
        head.appendChild(titleElement);
        
        String title = document.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testSetTitleWithNoTitleElement() {
        document.title("Test Title");
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Test Title", titleElement.text());
    }
    
    @Test
    public void testSetTitleWithExistingTitleElement() {
        Element titleElement = document.createElement("title");
        titleElement.text("Initial Title");
        
        Element head = document.head();
        if (head == null) {
            head = document.appendElement("head");
        }
        head.appendChild(titleElement);
        
        document.title("Updated Title");
        assertEquals("Updated Title", titleElement.text());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertEquals(Tag.valueOf("div"), element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormaliseWithoutHtmlElement() {
        Document normalizedDocument = document.normalise();
        
        assertNotNull(normalizedDocument.getElementsByTag("html").first());
        assertNotNull(normalizedDocument.head());
        assertNotNull(normalizedDocument.body());
    }
    
    @Test
    public void testNormaliseWithHtmlElement() {
        Element htmlElement = document.appendElement("html");
        Element head = htmlElement.appendElement("head");
        Element body = htmlElement.appendElement("body");
        
        Document normalizedDocument = document.normalise();
        
        assertEquals(htmlElement, normalizedDocument.getElementsByTag("html").first());
        assertEquals(head, normalizedDocument.head());
        assertEquals(body, normalizedDocument.body());
    }
    
    @Test
    public void testText() {
        Element body = document.appendElement("body");
        document.text("This is some text");
        
        assertEquals("This is some text", body.text());
    }
    
    @Test
    public void testOuterHtml() {
        Element body = document.appendElement("body");
        Element div = body.appendElement("div");
        div.text("Some content");
        
        assertEquals("<div>Some content</div>", div.outerHtml());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}