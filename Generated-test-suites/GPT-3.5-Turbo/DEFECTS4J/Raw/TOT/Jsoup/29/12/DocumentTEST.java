import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        
        Element html = doc.head();
        assertNotNull(html);

        Element head = doc.head();
        assertNotNull(head);

        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        
        Element head = doc.head();
        assertNull(head);
        
        head = doc.createElement("head");
        doc.appendChild(head);
        
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        
        Element body = doc.body();
        assertNull(body);
        
        body = doc.createElement("body");
        doc.appendChild(body);
        
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        String title = doc.title();
        assertEquals("", title);
        
        doc.title("Test Title");
        
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        
        Element html = doc.createElement("html");
        doc.appendChild(html);
        
        doc.normalise();
        
        Element body = doc.body();
        assertNotNull(body);
        
        Element head = doc.head();
        assertNotNull(head);
    }

    // Add more test cases for the remaining methods
    
}