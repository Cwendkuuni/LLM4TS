import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.child(0).tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();

        head.appendText("Head Text");
        body.appendText("Body Text");

        doc.normalise();

        assertEquals("Head Text Body Text", body.text());
        assertNull(head.text());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("This is a sample text");
        assertEquals("This is a sample text", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clonedDoc = doc.clone();
        assertNotNull(clonedDoc);
        assertEquals(doc.outputSettings(), clonedDoc.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset("UTF-8");
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}