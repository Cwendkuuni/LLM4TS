
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("https://example.com");
        Element head = doc.head();
        assertNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("https://example.com");
        Element body = doc.body();
        assertNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        
        // Test case for when title element is present
        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
        
        // Test case for when title element is not present
        assertEquals("", doc.title());
    }
    
    @Test
    public void testSetTitle() {
        Document doc = new Document("https://example.com");
        
        // Test case for adding title to head
        doc.setTitle("Test Title");
        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text());
        
        // Test case for updating existing title element
        doc.setTitle("New Title");
        assertEquals("New Title", titleEl.text());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        
        // Test case for creating a new element
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("https://example.com");
        doc.appendElement("title").text("Test Title");
        doc.appendElement("h1").text("Heading");
        doc.appendElement("p").text("Paragraph");
        
        doc.normalise();
        
        Element htmlEl = doc.getElementsByTag("html").first();
        assertNotNull(htmlEl);
        assertNotNull(htmlEl.getElementsByTag("head").first());
        assertNotNull(htmlEl.getElementsByTag("body").first());
        
        assertNull(doc.getElementsByTag("title").first());
        assertNotNull(htmlEl.getElementsByTag("title").first());
        
        assertNull(doc.getElementsByTag("h1").first());
        assertNotNull(htmlEl.getElementsByTag("h1").first());
        
        assertNull(doc.getElementsByTag("p").first());
        assertNotNull(htmlEl.getElementsByTag("p").first());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        
        // Test case for setting text of body
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://example.com");
        
        // Test case for outerHtml without wrapper tag
        doc.appendElement("p").text("Test Paragraph");
        assertEquals("<p>Test Paragraph</p>", doc.outerHtml());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        
        // Test case for escapeMode() and escapeMode(Entities.EscapeMode)
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        
        // Test case for charset() and charset(Charset)
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        
        // Test case for charset(String)
        outputSettings.charset("UTF-16");
        assertEquals(Charset.forName("UTF-16"), outputSettings.charset());
        
        // Test case for prettyPrint() and prettyPrint(boolean)
        assertTrue(outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        
        // Test case for indentAmount() and indentAmount(int)
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}

