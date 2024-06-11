import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadAccessor() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyAccessor() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        doc.normalise();
        // test for any specific assertion post normalization
    }

}