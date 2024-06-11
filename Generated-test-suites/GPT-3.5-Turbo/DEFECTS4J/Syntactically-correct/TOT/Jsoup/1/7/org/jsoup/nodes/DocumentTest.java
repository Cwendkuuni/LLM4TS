import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        
        assertEquals("html", shell.getElementsByTag("html").first().tagName());
        assertEquals("head", shell.getElementsByTag("head").first().tagName());
        assertEquals("body", shell.getElementsByTag("body").first().tagName());
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
        
        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        
        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        
        document.appendElement("title").text("Test Page");
        assertEquals("Test Page", document.title());
    }
    
    @Test
    public void testSetTitle() {
        document.title("New Title");
        assertEquals("New Title", document.getElementsByTag("title").first().text());
        
        document.appendElement("head").appendElement("title").text("Existing Title");
        document.title("Updated Title");
        assertEquals("Updated Title", document.getElementsByTag("title").first().text());
    }
    
    @Test
    public void testSetTitle_Null() {
        try {
            document.title(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Title cannot be null", e.getMessage());
        }
    }
    
    @Test
    public void testCreateInfoElement() {
        document.appendElement("body");
        Element info = document.createElement("div");
        
        assertNotNull(info);
        assertEquals("div", info.tagName());
        assertEquals("http://example.com", info.baseUri());
    }
    
    @Test
    public void testNormalise() {
        document.appendElement("html");
        document.normalise();
        
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
        
        document.appendElement("head").appendElement("title").text("Test Page");
        document.normalise();
        
        assertEquals("Test Page", document.title());
        
        document.normalise();
        
        assertEquals("head", document.getElementsByTag("html").first().child(0).tagName());
        assertEquals("body", document.getElementsByTag("html").first().child(1).tagName());
    }
    
    @Test
    public void testText() {
        document.appendElement("body").text("Hello, world!");
        assertEquals("Hello, world!", document.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        document.appendElement("body").appendElement("div").text("Hello, world!");
        assertEquals("<div>Hello, world!</div>", document.outerHtml());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}