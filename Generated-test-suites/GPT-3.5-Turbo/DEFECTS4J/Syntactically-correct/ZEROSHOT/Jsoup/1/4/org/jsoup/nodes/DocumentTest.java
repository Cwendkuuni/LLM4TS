import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testConstructor() {
        Document document = new Document("http://www.example.com");
        assertEquals("#root", document.tagName());
        assertEquals("http://www.example.com", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals("html", document.tagName());
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("http://www.example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Document document = new Document("http://www.example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("http://www.example.com");
        String emptyTitle = document.title();
        assertEquals("", emptyTitle);
        
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Document document = new Document("http://www.example.com");
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals("http://www.example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document document = new Document("http://www.example.com");
        document.appendElement("p").text("Text outside body");
        document.normalise();
        Element body = document.body();
        assertFalse(body.getElementsByTag("p").isEmpty());
        assertTrue(body.text().contains("Text outside body"));
    }
    
    @Test
    public void testOuterHtml() {
        Document document = new Document("http://www.example.com");
        assertEquals(document.html(), document.outerHtml());
    }
    
    @Test
    public void testText() {
        Document document = new Document("http://www.example.com");
        document.appendElement("p").text("Text 1");
        document.appendElement("p").text("Text 2");
        document.text("New Text");
        assertEquals("New Text", document.text());
        assertEquals(1, document.getAllElements().size());
    }
    
    @Test
    public void testNodeName() {
        Document document = new Document("http://www.example.com");
        assertEquals("#document", document.nodeName());
    }
}