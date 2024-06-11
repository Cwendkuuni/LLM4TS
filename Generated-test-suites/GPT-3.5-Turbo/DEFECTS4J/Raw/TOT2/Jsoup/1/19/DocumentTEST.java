import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tag().getName());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
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
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        doc.appendElement("p").text("Test paragraph");
        doc.normalise();
        assertEquals("<html><head></head><body> Test paragraph</body></html>", doc.outerHtml().trim());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Test body text");
        assertEquals("Test body text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }

}