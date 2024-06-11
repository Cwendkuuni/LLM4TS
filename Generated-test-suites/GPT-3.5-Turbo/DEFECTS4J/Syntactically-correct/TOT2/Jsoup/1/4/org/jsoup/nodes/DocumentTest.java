import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.children().get(0).tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.createElement("div"));
        assertEquals("div", doc.createElement("div").tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test text content");
        assertEquals("Test text content", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    // Add additional edge cases and exception handling scenarios tests here

}