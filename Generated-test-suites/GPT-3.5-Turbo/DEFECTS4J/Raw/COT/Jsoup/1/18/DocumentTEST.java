
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        
        Element head = doc.head();
        assertNull(head);
        
        Element html = doc.appendElement("html");
        html.appendElement("head");
        
        head = doc.head();
        assertNotNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        
        Element body = doc.body();
        assertNull(body);
        
        Element html = doc.appendElement("html");
        html.appendElement("body");
        
        body = doc.body();
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        
        String title = doc.title();
        assertEquals("", title);
        
        Element titleEl = new Element(Tag.valueOf("title"), doc.baseUri());
        titleEl.text("Test Title");
        doc.head().appendChild(titleEl);
        
        title = doc.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testSetTitle() {
        Document doc = new Document("https://www.example.com");
        
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
        
        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("New Title", titleEl.text());
    }
    
    @Test
    public void testcreateElement() {
        Document doc = new Document("https://www.example.com");
        
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        
        doc.normalise();
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://www.example.com");
        
        String outerHtml = doc.outerHtml();
        assertEquals("", outerHtml);
        
        Element div = doc.appendElement("div");
        div.text("Test Div");
        
        outerHtml = doc.outerHtml();
        assertEquals("<div>Test Div</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        
        doc.text("Test Text");
        
        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
        
        assertEquals("Test Text", body.text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("https://www.example.com");
        
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }

}

