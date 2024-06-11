import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tag().toString());
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.children().first().tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://example.com");
        doc.body().appendText("Some text");
        doc.title("Test Title");

        Document normalisedDoc = doc.normalise();
        assertEquals("Some textTest Title", normalisedDoc.body().text());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        doc.text("Example text");
        assertEquals("Example text", doc.body().text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        doc.createElement("div");
        assertEquals("div", doc.getElementsByTag("div").first().tagName());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("https://example.com");
        assertEquals("#document", doc.nodeName());
    }
}