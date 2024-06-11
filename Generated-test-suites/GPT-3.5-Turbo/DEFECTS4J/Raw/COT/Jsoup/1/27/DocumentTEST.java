
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://example.com");
        assertEquals("#root", document.tagName());
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        assertEquals("html", document.tagName());
        assertEquals("https://example.com", document.baseUri());
        assertEquals(1, document.getElementsByTag("head").size());
        assertEquals(1, document.getElementsByTag("body").size());
    }

    @Test
    public void testHeadAccessor() {
        Document document = new Document("https://example.com");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        assertEquals(head, document.head());
        assertEquals(null, document.body());
    }

    @Test
    public void testBodyAccessor() {
        Document document = new Document("https://example.com");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
        assertEquals(null, document.head());
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://example.com");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        head.appendElement("title").text("Page Title");
        assertEquals("Page Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("https://example.com");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        document.title("New Title");
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("https://example.com");
        Element element = document.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://example.com");
        document.normalise();
        // Add assertions here
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("https://example.com");
        // Add assertions here
    }

    @Test
    public void testText() {
        Document document = new Document("https://example.com");
        Element body = document.appendElement("body");
        document.text("Hello World");
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("https://example.com");
        assertEquals("#document", document.nodeName());
    }
}