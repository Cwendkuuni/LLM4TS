import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertEquals("#root", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadAccessor() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc.head());
    }

    @Test
    public void testBodyAccessor() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("https://example.com");
        assertEquals("", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("https://example.com");
        doc.title("Page Title");
        assertEquals("Page Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://example.com");
        doc.appendElement("p").text("Paragraph Text");
        doc.appendElement("div").text("Division Text");
        doc.normalise();
        assertEquals("Paragraph Text Division Text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://example.com");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        doc.text("Document Text");
        assertEquals("Document Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://example.com");
        assertEquals("#document", doc.nodeName());
    }
}