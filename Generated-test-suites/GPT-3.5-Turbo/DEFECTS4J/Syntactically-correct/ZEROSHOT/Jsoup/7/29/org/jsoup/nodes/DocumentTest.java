import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDocument = Document.createShell("https://example.com");
        assertEquals("#root", shellDocument.nodeName());
        assertEquals("https://example.com", shellDocument.baseUri());
        assertNotNull(shellDocument.head());
        assertNotNull(shellDocument.body());
        assertNotNull(shellDocument.head().getElementsByTag("title").first());
    }

    @Test
    public void testHead() {
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitle() {
        String title = "Example Title";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    public void testTitleNoElement() {
        Document noTitleDocument = new Document("https://example.com");
        assertEquals("", noTitleDocument.title());
    }

    @Test
    public void testTitleNull() {
        try {
            document.title(null);
            fail("Expected exception not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be null", e.getMessage());
        }
    }

    @Test
    public void testTitleUpdateExistingElement() {
        String title = "Existing Title";
        document.appendElement("head").appendElement("title").text(title);

        String newTitle = "New Title";
        document.title(newTitle);
        assertEquals(newTitle, document.title());
    }

    @Test
    public void testTitleAddToHead() {
        String title = "Existing Title";
        document.appendElement("body").appendElement("h1").text("Example Heading");

        String newTitle = "New Title";
        document.title(newTitle);

        assertNotNull(document.head().getElementsByTag("title"));
        assertEquals(newTitle, document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testTitleElementIsNull() {
        String title = "Existing Title";
        document.head().appendElement("h1").text("Example Heading");

        String newTitle = "New Title";
        document.title(newTitle);

        assertNotNull(document.head().getElementsByTag("title"));
        assertEquals(newTitle, document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        assertNotNull(document.createElement(tagName));
    }

    @Test
    public void testNormalise() {
        Document normDocument = new Document("https://example.com");
        
        normDocument.normalise();
        
        assertNotNull(normDocument.getElementsByTag("html"));
        assertNotNull(normDocument.head());
        assertNotNull(normDocument.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document normDocument = new Document("https://example.com");
        normDocument.appendElement("head");
        normDocument.appendElement("body");
        normDocument.normalise();

        assertEquals("", normDocument.head().text());
        assertEquals("", normDocument.body().text());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Example Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        assertEquals(Entities.EscapeMode.base, document.outputSettings().escapeMode());

        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        document.outputSettings().escapeMode(escapeMode);
        assertEquals(escapeMode, document.outputSettings().escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());

        Charset charset = Charset.forName("ISO-8859-1");
        document.outputSettings().charset(charset);
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testOutputSettingsCharsetByName() {
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());

        String charsetName = "ISO-8859-1";
        document.outputSettings().charset(charsetName);
        assertEquals(Charset.forName(charsetName), document.outputSettings().charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        assertTrue(document.outputSettings().prettyPrint());

        document.outputSettings().prettyPrint(false);
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        assertEquals(1, document.outputSettings().indentAmount());

        int indentAmount = 2;
        document.outputSettings().indentAmount(indentAmount);
        assertEquals(indentAmount, document.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettingsEncoder() {
        assertNotNull(document.outputSettings().encoder());
    }

    @Test
    public void testOutputSettingsGetter() {
        assertNotNull(document.outputSettings());
        assertEquals(Document.OutputSettings.class, document.outputSettings().getClass());
    }
}