import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testAddTitleToHead() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertNotNull(doc.head().getElementsByTag("title").first());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("div");
        assertNotNull(newElement);
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        assertTrue(doc.getElementsByTag("html").size() > 0);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testTextInBody() {
        Document doc = new Document("http://example.com");
        doc.text("Test text");
        assertEquals("Test text", doc.body().text());
    }
}