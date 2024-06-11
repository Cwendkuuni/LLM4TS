import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        
        assertEquals("#root", doc.tagName());
        assertEquals("https://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHead() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertNotNull(doc.head());
    }
    
    @Test
    public void testBody() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertNotNull(doc.body());
    }
    
    @Test
    public void testTitle() {
        Document doc = Document.createShell("https://www.example.com");
        
        doc.title("Document Title");
        assertEquals("Document Title", doc.title());
        
        doc.title("");
        assertEquals("", doc.title());
    }
    
    @Test
    public void testcreateElement() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertNotNull(doc.createElement("div"));
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        
        doc.normalise();
        
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertEquals(doc.html(), doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        
        doc.text("Document text");
        assertEquals("Document text", doc.text());
        
        doc.text("");
        assertEquals("", doc.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertNotNull(doc.outputSettings());
    }

    // Additional test cases for edge cases and exception handling:
    
    @Test
    public void testTitle_NullInput() {
        Document doc = Document.createShell("https://www.example.com");
        
        try {
            doc.title(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    @Test
    public void testHead_NotFound() {
        Document doc = new Document("https://www.example.com");
        
        assertNull(doc.head());
    }
    
    @Test
    public void testBody_NotFound() {
        Document doc = new Document("https://www.example.com");
        
        assertNull(doc.body());
    }
    
    @Test
    public void testcreateElement_InvalidTagName() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertNull(doc.createElement("invalid-tag"));
    }
    
    @Test
    public void testNormalise_NoHtmlElement() {
        Document doc = new Document("https://www.example.com");
        
        doc.normalise();
        
        assertNotNull(doc.getElementsByTag("html"));
    }

}