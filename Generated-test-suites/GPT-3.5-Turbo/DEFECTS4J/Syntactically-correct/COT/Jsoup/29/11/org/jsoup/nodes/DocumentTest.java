
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Hello, World!");
        assertEquals("Hello, World!", doc.title());
    }

    @Test
    public void testTitleWithNoTitleElement() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleNullArgument() {
        Document doc = Document.createShell("http://example.com");
        assertThrows(NullPointerException.class, () -> {
            doc.title(null);
        });
    }

    @Test
    public void testTitleExistingTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Old Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testTitleAddToHead() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Hello, World!");
        Element titleElement = doc.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Hello, World!", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.appendElement("div");
        element.text("Some text");
        doc.normalise();
        assertEquals("Some text", doc.body().text());
        assertFalse(doc.html().contains("div"));
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.appendElement("p");
        element.text("Hello, World!");
        assertEquals("<p>Hello, World!</p>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Hello, World!");
        assertEquals("Hello, World!", doc.text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://example.com");
        Document clone = doc.clone();
        assertNotSame(doc, clone);
        assertEquals(doc.outerHtml(), clone.outerHtml());
        assertEquals(doc.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        doc.outputSettings(outputSettings);
        assertSame(outputSettings, doc.outputSettings());
        assertFalse(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
        doc.quirksMode(Document.QuirksMode.quirks);
        assertSame(Document.QuirksMode.quirks, doc.quirksMode());
    }
}
