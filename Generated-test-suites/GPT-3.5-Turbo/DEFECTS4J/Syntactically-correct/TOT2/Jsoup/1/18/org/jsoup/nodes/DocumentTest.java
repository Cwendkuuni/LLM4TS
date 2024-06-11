import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#root", doc.tagName());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleNull() {
        Document doc = new Document("https://www.example.com");
        assertThrows(IllegalArgumentException.class, () -> doc.title(null));
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        doc.normalise();
        assertTrue(doc.html().contains("<html>"));
        assertTrue(doc.html().contains("<head>"));
        assertTrue(doc.html().contains("<body>"));
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element elem = doc.createElement("a");
        assertEquals("a", elem.tagName());
        assertEquals("https://www.example.com", elem.baseUri());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://www.example.com");
        assertEquals(doc.html(), doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }
}