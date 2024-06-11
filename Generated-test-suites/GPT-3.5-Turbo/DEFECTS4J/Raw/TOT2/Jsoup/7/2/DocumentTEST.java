import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(doc, doc.normalise());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(doc, doc.text("Hello World"));
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.outputSettings());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(Entities.EscapeMode.base, doc.outputSettings().escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document doc = Document.createShell("http://example.com");
        assertTrue(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(1, doc.outputSettings().indentAmount());
    }
}