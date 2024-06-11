import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertNotNull(shell);
        assertEquals("https://www.example.com", shell.baseUri());
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("body", shell.body().parent().nodeName());
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
        
        // Add head element
        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        
        // Add body element
        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        
        // Add title element
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
        
        // Update title element
        document.title("New Title");
        assertEquals("New Title", document.title());
    }
    
    @Test
    public void testcreateElement() {
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }
    
    @Test
    public void testnormalise() {
        // TODO: Implement the test for the normalise() method
    }
    
    // TODO: Implement tests for other methods in the Document class
    
}