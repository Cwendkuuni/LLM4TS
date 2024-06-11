import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("html", shell.body().parent().nodeName());
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
        document.head().appendElement("title");
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
        document.body().appendElement("div");
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
        document.title("New Title");
        assertEquals("New Title", document.title());
    }
    
    @Test
    public void testcreateElement() {
        assertNotNull(document.createElement("div"));
        assertEquals("div", document.createElement("div").nodeName());
    }
    
    @Test
    public void testNormalise() {
        Document shell = Document.createShell("https://example.com");
        shell.append("<head><title>Test Title</title></head><body>Test Body</body>");
        shell.normalise();
        Elements headElements = shell.getElementsByTag("head");
        Elements bodyElements = shell.getElementsByTag("body");
        assertEquals(1, headElements.size());
        assertEquals(1, bodyElements.size());
        assertEquals("Test Title", headElements.first().getElementsByTag("title").text());
        assertEquals("Test Body", bodyElements.first().text());
    }
    
    @Test
    public void testOuterHtml() {
        document.body().appendElement("div");
        assertEquals("<div></div>", document.outerHtml());
    }
    
    @Test
    public void testText() {
        document.body().text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
    
    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.outputSettings().charset(), clone.outputSettings().charset());
        assertEquals(document.outputSettings().escapeMode(), clone.outputSettings().escapeMode());
        assertEquals(document.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
        assertEquals(document.title(), clone.title());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        document.outputSettings(outputSettings);
        assertFalse(document.outputSettings().prettyPrint());
    }
    
    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}