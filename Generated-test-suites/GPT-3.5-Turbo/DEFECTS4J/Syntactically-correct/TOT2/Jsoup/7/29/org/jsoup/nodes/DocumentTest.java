import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("www.example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("www.example.com");
        assertNotNull(document);
        assertTrue(document.getElementsByTag("html").size() > 0);
        assertTrue(document.getElementsByTag("head").size() > 0);
        assertTrue(document.getElementsByTag("body").size() > 0);
    }

    @Test
    public void testHeadAccessor() {
        Document document = new Document("www.example.com");
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBodyAccessor() {
        Document document = new Document("www.example.com");
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("www.example.com");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("www.example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
    }

    @Test
    public void testNormalise() {
        Document document = new Document("www.example.com");
        Document normalisedDocument = document.normalise();
        assertNotNull(normalisedDocument);
    }

    @Test
    public void testText() {
        Document document = new Document("www.example.com");
        document.text("Test text");
        assertTrue(document.body().text().contains("Test text"));
    }

}