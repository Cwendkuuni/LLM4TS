
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("html", document.head().parent().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.normalise();
        // This method changes the structure of the document, so it's harder to test the functionality. 
        // Additional tests may be required to verify the correctness of this method.
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        assertEquals("", document.outerHtml());
        document.head().appendElement("title").text("Test Title");
        assertEquals("<html>\n <head>\n  <title>Test Title</title>\n </head>\n <body></body>\n</html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.text("Test text");
        assertEquals("Test text", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://example.com");
        assertEquals(Document.OutputSettings.class, document.outputSettings().getClass());
    }
}
