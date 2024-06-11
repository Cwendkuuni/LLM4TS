import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
    
    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tag().getName());
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }
    
    @Test
    public void testHeadAccessor() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
    }
    
    @Test
    public void testBodyAccessor() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
    }
    
    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("div");
        assertEquals("div", newElement.tagName());
        assertEquals("http://example.com", newElement.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        // Add specific assertions here based on expected normalization behavior
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testSetText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Body Text");
        assertEquals("Test Body Text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}