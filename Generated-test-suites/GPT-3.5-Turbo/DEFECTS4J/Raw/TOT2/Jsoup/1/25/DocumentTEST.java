import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.head().parent().nodeName());
        assertEquals("html", doc.body().parent().nodeName());
    }

    @Test
    public void testHeadAccessor() {
        Document doc = new Document("https://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyAccessor() {
        Document doc = new Document("https://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://example.com");
        doc.createElement("head").text("Head Content");
        doc.createElement("body").text("Body Content");
        doc.normalise();
        assertEquals("Head Content", doc.head().text());
        assertEquals("Body Content", doc.body().text());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        doc.text("Document Text");
        assertEquals("Document Text", doc.body().text());
    }

}