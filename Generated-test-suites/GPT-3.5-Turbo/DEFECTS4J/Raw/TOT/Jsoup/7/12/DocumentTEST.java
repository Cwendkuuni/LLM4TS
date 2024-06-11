import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("html", shell.tagName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitleWithTitleElement() {
        document.head().appendElement("title").text("Example Title");
        assertEquals("Example Title", document.title());
    }

    @Test
    public void testTitleWithoutTitleElement() {
        assertEquals("", document.title());
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Old Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testSetTitleWithoutTitleElement() {
        document.title("New Title");
        assertEquals("New Title", document.title());
        assertNotNull(document.head().getElementsByTag("title").first());
    }

    @Test
    public void testCreateElement() {
        Document elementDocument = new Document("http://example.com");
        elementDocument.createElement("div");
        assertNotNull(elementDocument.body().getElementsByTag("div").first());
    }

    @Test
    public void testNormalise() {
        Document normalisedDocument = Document.createShell("http://example.com");
        normalisedDocument.normalise();
        assertNotNull(normalisedDocument.head());
        assertNotNull(normalisedDocument.body());
        assertFalse(normalisedDocument.getElementsByTag("head").isEmpty());
        assertFalse(normalisedDocument.getElementsByTag("body").isEmpty());
        assertEquals("", normalisedDocument.head().text());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("span").text("Example Text");
        assertEquals("<span>Example Text</span>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings().escapeMode(Document.Entities.EscapeMode.extended);
        assertEquals(Document.Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings().prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings().indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

}