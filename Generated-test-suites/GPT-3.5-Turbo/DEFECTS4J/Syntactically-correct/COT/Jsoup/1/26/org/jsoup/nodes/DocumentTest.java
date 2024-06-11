
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testDocument() {
        Document document = new Document("http://example.com");
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element headElement = new Element(Tag.valueOf("head"), "http://example.com");
        document.appendChild(headElement);
        assertEquals(headElement, document.head());
    }
    
    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element bodyElement = new Element(Tag.valueOf("body"), "http://example.com");
        document.appendChild(bodyElement);
        assertEquals(bodyElement, document.body());
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        Element titleElement = new Element(Tag.valueOf("title"), "http://example.com");
        titleElement.text("Test Title");
        document.appendChild(titleElement);
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testSetTitleElementPresent() {
        Document document = new Document("http://example.com");
        Element titleElement = new Element(Tag.valueOf("title"), "http://example.com");
        document.head().appendChild(titleElement);
        document.title("Test Title");
        assertEquals("Test Title", titleElement.text());
    }
    
    @Test
    public void testSetTitleElementNotPresent() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Test Title", titleElement.text());
    }
    
    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element htmlElement = new Element(Tag.valueOf("html"), "http://example.com");
        Element headElement = new Element(Tag.valueOf("head"), "http://example.com");
        Element bodyElement = new Element(Tag.valueOf("body"), "http://example.com");
        document.appendChild(htmlElement);
        htmlElement.appendChild(headElement);
        htmlElement.appendChild(bodyElement);
        
        Element textElement = new Element(Tag.valueOf("text"), "http://example.com");
        textElement.text("Test Text");
        document.appendChild(textElement);
        
        document.normalise();
        
        assertEquals("<html><head></head><body>Test Text</body></html>", document.outerHtml());
    }
    
    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test text");
        assertEquals("Test text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }
}
