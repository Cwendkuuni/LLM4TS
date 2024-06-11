import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://www.example.com");
        
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
        assertEquals("html", document.getElementsByTag("html").first().nodeName());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("https://www.example.com");
        Element head = document.head();
        
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("https://www.example.com");
        Element body = document.body();
        
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("https://www.example.com");
        document.appendElement("head").appendElement("title").text("Test Title");
        
        String title = document.title();
        
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("https://www.example.com");
        document.title("Test Title");
        
        Element title = document.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("title", title.nodeName());
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("https://www.example.com");
        Element element = document.createElement("a");
        
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://www.example.com");
        Element element = document.appendElement("div");
        element.appendText("Test Text");
        
        document.normalise();
        
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
        assertNull(document.getElementsByTag("div").first());
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("https://www.example.com");
        document.appendElement("div").text("Test Text");
        
        String outerHtml = document.outerHtml();
        
        assertEquals("<div>Test Text</div>", outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("https://www.example.com");
        document.body().text("Test Text");
        
        String text = document.body().text();
        
        assertEquals("Test Text", text);
    }

}