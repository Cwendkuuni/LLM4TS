import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://www.example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        Element titleElement = doc.createElement("title");
        titleElement.text("Test Title");
        doc.head().appendChild(titleElement);
        doc.normalise();
        assertEquals(1, doc.head().children().size());
        assertEquals(1, doc.body().children().size());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}