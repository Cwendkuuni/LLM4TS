import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testConstructor_ValidBaseUri() {
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell_ValidBaseUri() {
        Document shellDoc = Document.createShell("https://www.example.com");

        assertNotNull(shellDoc);
        assertEquals("https://www.example.com", shellDoc.baseUri());

        assertEquals("html", shellDoc.children().first().tagName());
        assertEquals("head", shellDoc.head().tagName());
        assertEquals("body", shellDoc.body().tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShell_NullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testHead_EmptyDocument() {
        assertNull(document.head());

        document.appendElement("head");
        assertNotNull(document.head());
    }

    @Test
    public void testBody_EmptyDocument() {
        assertNull(document.body());

        document.appendElement("body");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle_EmptyDocument() {
        assertEquals("", document.title());

        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitle_NullTitle() {
        document.title(null);
    }

    @Test
    public void testTitle_UpdateExisting() {
        document.appendElement("head").appendElement("title").text("Old Title");

        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitle_AddToHead() {
        document.title("New Title");
        assertEquals("New Title", document.title());
        assertEquals("title", document.getElementsByTag("title").first().tagName());
    }

    @Test
    public void testCreateElement_ValidTagName() {
        Tag tag = Tag.valueOf("div");
        assertNotNull(document.createElement(tag.getName()));
        assertEquals(tag.getName(), document.createElement(tag.getName()).tagName());
    }

    @Test
    public void testNormalise_EmptyDocument() {
        Document normalisedDoc = document.normalise();

        assertNotNull(normalisedDoc);
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertEquals("html", normalisedDoc.children().first().tagName());
    }

    @Test
    public void testNormalise_ExistingElements() {
        document.appendElement("head");
        document.appendElement("body").appendText("Text content");

        Document normalisedDoc = document.normalise();

        assertNotNull(normalisedDoc);
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertEquals("", normalisedDoc.head().text());
        assertEquals("Text content", normalisedDoc.body().text());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("head");
        document.appendElement("body").appendText("Text content");

        assertEquals("<head></head><body>Text content</body>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.appendElement("body").appendElement("p").text("Existing text");

        document.text("New text");
        assertEquals("New text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings_EscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettings_Charset() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertEquals("UTF-8", outputSettings.charset().name());

        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettings_PrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertTrue(outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettings_IndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertEquals(1, outputSettings.indentAmount());

        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());

        outputSettings.indentAmount(0);
        assertEquals(0, outputSettings.indentAmount());
    }

}