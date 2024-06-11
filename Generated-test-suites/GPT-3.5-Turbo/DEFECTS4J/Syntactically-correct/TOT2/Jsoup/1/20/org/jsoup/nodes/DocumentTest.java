import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.title("Testing");
        assertEquals("Testing", doc.title());
    }

    @Test
    public void testTitleNull() {
        Document doc = new Document("https://www.example.com");
        assertThrows(IllegalArgumentException.class, () -> {
            doc.title(null);
        });
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc.createElement("div"));
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc.normalise());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Testing text");
        assertEquals("Testing text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }
}