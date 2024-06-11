import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("https://example.com");
        
        assertEquals("#root", document.tagName());
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
        assertEquals("html", document.tagName());
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testHead() {
        Document document = new Document("https://example.com");
        
        assertNull(document.head());
        
        document.appendElement("head");
        
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        Document document = new Document("https://example.com");
        
        assertNull(document.body());
        
        document.appendElement("body");
        
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://example.com");
        
        assertEquals("", document.title());
        
        document.title("Test Title");
        
        assertEquals("Test Title", document.title());
        
        document.appendElement("head").appendElement("title").text("Another Title");
        
        assertEquals("Another Title", document.title());
    }

    @Test
    public void testcreateElement() {
        Document document = new Document("https://example.com");
        
        assertNotNull(document.createElement("div"));
        assertEquals("div", document.createElement("div").tagName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://example.com");
        
        assertNull(document.select("html").first());
        
        document.normalise();
        
        assertNotNull(document.select("html").first());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testText() {
        Document document = new Document("https://example.com");
        
        assertEquals("https://example.com", document.text(""));
        
        document.appendElement("body").text("Test Text");
        
        assertEquals("Test Text", document.text(""));
    }

    @Test
    public void testNodeName() {
        Document document = new Document("https://example.com");
        
        assertEquals("#document", document.nodeName());
    }
}