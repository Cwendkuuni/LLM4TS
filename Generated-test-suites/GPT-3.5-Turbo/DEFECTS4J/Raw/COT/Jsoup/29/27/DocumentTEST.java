
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertEquals("html", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    @Test
    public void testHead() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document.head());
    }
    
    @Test
    public void testBody() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document.body());
    }
    
    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        String title = "Example Title";
        document.title(title);
        assertEquals(title, document.title());
    }
    
    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        String text = "Example Text";
        document.text(text);
        assertEquals(text, document.text());
    }
    
    // Add more test cases for the remaining methods
    
}

