import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document);    
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document.head());
    }
    
    @Test
    public void testBody() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document.body());
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("http://www.example.com");
        String title = document.title();
        assertNotNull(title);
    }
    
    @Test
    public void testSetTitle() {
        Document document = new Document("http://www.example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testCreateElement() {
        Document document = new Document("http://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }
    
    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://www.example.com");
        document.normalise();
        // Assert specific normalisation behavior
    }
    
    @Test
    public void testOuterHtml() {
        Document document = new Document("http://www.example.com");
        String html = document.outerHtml();
        assertNotNull(html);
    }
    
    @Test
    public void testText() {
        Document document = new Document("http://www.example.com");
        String text = document.text("test");
        assertNotNull(text);
        assertEquals("test", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document document = new Document("http://www.example.com");
        String nodeName = document.nodeName();
        assertNotNull(nodeName);
        assertEquals("#document", nodeName);
    }
}