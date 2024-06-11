
import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("html", doc.head().nodeName());
        assertEquals("head", doc.head().parent().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals("html", doc.body().parent().nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();

        assertFalse(doc.select("html").isEmpty());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        doc.body().text("Test Body");
        assertEquals("<head><title>Test Title</title></head><body>Test Body</body>", doc.outerHtml());
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

