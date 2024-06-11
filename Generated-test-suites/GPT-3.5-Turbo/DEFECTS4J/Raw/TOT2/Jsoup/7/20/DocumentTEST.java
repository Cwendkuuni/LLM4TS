import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

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
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.head().text("Head Text");
        doc.body().text("Body Text");
        doc.normalise();
        assertEquals("", doc.head().text());
        assertEquals("Head Text Body Text", doc.body().text());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Document Text");
        assertEquals("Document Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.outputSettings());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        doc.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
    }
}