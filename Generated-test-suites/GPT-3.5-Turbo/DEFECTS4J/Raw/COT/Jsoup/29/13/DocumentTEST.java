
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("https://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");
        assertNotNull(document);
        assertEquals("html", document.childNodes.get(0).nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("https://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("https://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("https://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("https://example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://example.com");
        document.title("Test Title");
        document.normalise();
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testClone() {
        Document document = Document.createShell("https://example.com");
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.baseUri(), clone.baseUri());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("https://example.com");
        assertNull(document.outputSettings().escapeMode());
        document.outputSettings().escapeMode(Document.OutputSettings.EscapeMode.base);
        assertEquals(Document.OutputSettings.EscapeMode.base, document.outputSettings().escapeMode());
    }

    @Test
    public void testQuirksMode() {
        Document document = Document.createShell("https://example.com");
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}
