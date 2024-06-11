import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertTrue(shell.getElementsByTag("html").size() == 1);
        assertTrue(shell.getElementsByTag("head").size() == 1);
        assertTrue(shell.getElementsByTag("body").size() == 1);
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        assertEquals("New Title", document.title());
        assertEquals("New Title", document.getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        assertEquals(Tag.valueOf(tagName).toString(), document.createElement(tagName).tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();

        assertTrue(doc.getElementsByTag("html").size() == 1);
        assertTrue(doc.getElementsByTag("head").size() == 1);
        assertTrue(doc.getElementsByTag("body").size() == 1);
    }

    @Test
    public void testText() {
        String text = "Example Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("div");
        assertEquals("<div></div>", document.outerHtml());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = Document.QuirksMode.limitedQuirks;
        document.quirksMode(quirksMode);
        assertEquals(quirksMode, document.quirksMode());
    }
}