import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://www.example.com");
        assertNotNull(shell);
        assertEquals("http://www.example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element headElement = document.head();
        assertNull(headElement);
        
        Element head = new Element(Tag.valueOf("head"), "http://www.example.com");
        document.appendChild(head);
        
        headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }
    
    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNull(bodyElement);
        
        Element body = new Element(Tag.valueOf("body"), "http://www.example.com");
        document.appendChild(body);
        
        bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }
    
    // Add more test cases for other methods
    
}