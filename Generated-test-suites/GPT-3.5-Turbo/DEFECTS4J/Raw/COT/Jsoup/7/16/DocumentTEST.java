import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.html());
        assertTrue(doc.html().contains("<html>"));
        assertTrue(doc.html().contains("<head></head>"));
        assertTrue(doc.html().contains("<body></body>"));
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        assertNull(doc.head());
        doc.head().appendElement("title").text("Test Title");
        assertNotNull(doc.head());
        assertEquals("title", doc.head().children().first().nodeName());
        assertEquals("Test Title", doc.head().text());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        assertNull(doc.body());
        doc.body().appendElement("div").text("Test Content");
        assertNotNull(doc.body());
        assertEquals("div", doc.body().children().first().nodeName());
        assertEquals("Test Content", doc.body().text());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testcreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.body().append("<span>Test Content</span>");
        doc.normalise();
        assertNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("span", doc.body().children().first().nodeName());
        assertEquals("Test Content", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.body().append("<div>Test Content</div>");
        assertEquals("<div>Test Content</div>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.body().text("Test Content");
        assertEquals("Test Content", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        assertNotNull(settings);
        assertEquals(Document.OutputSettings.EscapeMode.base, settings.escapeMode());
        assertEquals("UTF-8", settings.charset().displayName());
        assertEquals(true, settings.prettyPrint());
        assertEquals(1, settings.indentAmount());
    }
}