import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        
        assertEquals("", doc.title());
        
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        
        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());
    }
    
    @Test
    public void testTitleNull() {
        Document doc = new Document("http://example.com");
        
        assertNull(doc.getElementsByTag("title").first());
        
        doc.title(null);
        assertNotNull(doc.getElementsByTag("title").first());
        assertEquals("", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        
        Element div = doc.createElement("div");
        div.text("Text 1");
        doc.body().appendChild(div);
        
        doc.normalise();
        
        assertEquals("Text 1", doc.body().text());
        assertEquals("", doc.head().text());
        assertEquals("", doc.text());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        
        Element div = doc.createElement("div");
        div.text("Text 1");
        doc.body().appendChild(div);
        
        assertEquals("<div>Text 1</div>", doc.html());
        assertEquals("Text 1", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        
        assertEquals("#document", doc.nodeName());
    }
}