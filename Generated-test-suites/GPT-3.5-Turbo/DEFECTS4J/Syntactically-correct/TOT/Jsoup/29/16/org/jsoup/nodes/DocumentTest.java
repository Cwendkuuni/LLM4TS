import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    // Test the constructor
    @Test
    public void testConstructor() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    // Test the createShell() method
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    // Test the head() method
    @Test
    public void testHead() {
        Document doc = Document.createShell("https://www.example.com");
        Element head = doc.head();
        assertEquals("head", head.nodeName());
    }

    // Test the body() method
    @Test
    public void testBody() {
        Document doc = Document.createShell("https://www.example.com");
        Element body = doc.body();
        assertEquals("body", body.nodeName());
    }

    // Test the title() method
    @Test
    public void testTitle() {
        Document doc = Document.createShell("https://www.example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    // Test the createElement() method
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("https://www.example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    // Test the normalise() method
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        doc.normalise();
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        assertEquals("html", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    // Test the outerHtml() method
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("https://www.example.com");
        String outerHtml = doc.outerHtml();
        assertEquals("", outerHtml);
    }

    // Test the text() method
    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    // Test the nodeName() method
    @Test
    public void testNodeName() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }

    // Test the clone() method
    @Test
    public void testClone() {
        Document doc = Document.createShell("https://www.example.com");
        Document clone = doc.clone();
        assertEquals(doc.outerHtml(), clone.outerHtml());
    }

    // Test the escapeMode() method
    @Test
    public void testEscapeMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
    }

    // Test the charset() method
    @Test
    public void testCharset() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", settings.charset().name());
    }

    // Test the prettyPrint() method
    @Test
    public void testPrettyPrint() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());
    }

    // Test the indentAmount() method
    @Test
    public void testIndentAmount() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.indentAmount(4);
        assertEquals(4, settings.indentAmount());
    }

    // Test the outputSettings() method
    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://www.example.com");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(false);
        doc.outputSettings(settings);
        assertFalse(doc.outputSettings().prettyPrint());
    }

    // Test the quirksMode() method
    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("https://www.example.com");
        doc.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, doc.quirksMode());
    }
}