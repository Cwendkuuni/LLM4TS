import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;
import java.nio.charset.Charset;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body").appendText("Some Text");
        doc.normalise();
        assertEquals("Some Text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body").appendText("Some Text");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Some Text");
        assertEquals("Some Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document().outputSettings();
        assertEquals(Document.OutputSettings.escapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

}