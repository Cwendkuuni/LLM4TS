import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://example.com");
        assertNotNull(document);
        assertEquals("#root", document.tag().toString());
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        assertNotNull(document);
        assertEquals("html", document.child(0).tagName());
        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testGetHead() {
        Document document = new Document("https://example.com");
        assertNotNull(document.head());
    }

    @Test
    public void testGetBody() {
        Document document = new Document("https://example.com");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        Document document = new Document("https://example.com");
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("https://example.com");
        assertNotNull(document.createElement("a"));
    }

    @Test
    public void testNormalise() {
        Document document = new Document("https://example.com");
        document.body().appendText("Test Text");
        document.normalise();
        assertEquals("Test Text", document.body().text());
    }
}