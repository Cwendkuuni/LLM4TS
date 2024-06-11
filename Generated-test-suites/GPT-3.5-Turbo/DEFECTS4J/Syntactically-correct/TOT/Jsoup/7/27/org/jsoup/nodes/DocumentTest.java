import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }

    @Test
    public void testCreateShellValidBaseUri() {
        Document shellDocument = Document.createShell("http://www.example.com");
        assertNotNull(shellDocument);
        assertEquals(Tag.valueOf("html"), shellDocument.tagName());
        assertNotNull(shellDocument.head());
        assertNotNull(shellDocument.body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellInvalidBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testHeadNoElementPresent() {
        assertNull(document.head());
    }

    @Test
    public void testHeadWithElementPresent() {
        document.appendElement("head");
        assertNotNull(document.head());
    }

    @Test
    public void testBodyNoElementPresent() {
        assertNull(document.body());
    }

    @Test
    public void testBodyWithElementPresent() {
        document.appendElement("body");
        assertNotNull(document.body());
    }

    @Test
    public void testTitleNoTitleElementPresent() {
        assertEquals("", document.title());
    }

    @Test
    public void testTitleWithTitleElementPresent() {
        document.appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleWithNullTitle() {
        document.title(null);
    }

    @Test
    public void testSetTitleWithTitleElementPresent() {
        document.appendElement("title").text("Old Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testSetTitleWithoutTitleElementPresent() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertNotNull(document.head().getElementsByTag("title"));
    }

    @Test
    public void testTitleWithMultipleTitleElements() {
        document.appendElement("title").text("First Title");
        document.appendElement("title").text("Second Title");
        assertEquals("First Title", document.title());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseWithExistingHtmlElement() {
        document.appendElement("html");
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseWithExistingHeadElement() {
        document.appendElement("html").appendElement("head");
        document.normalise();
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseWithExistingBodyElement() {
        document.appendElement("html").appendElement("body");
        document.normalise();
        assertNotNull(document.head());
    }

    @Test
    public void testNormaliseTextNodes() {
        document.appendElement("html").appendElement("body").appendText("Test Text");
        document.normalise();
        assertEquals("", document.text());
        assertNotNull(document.body().text());
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNormaliseTextNodesWithBlankText() {
        document.appendElement("html").appendElement("body").appendText("    ");
        document.normalise();
        assertEquals("", document.text());
        assertNotNull(document.body().text());
        assertEquals("", document.body().text());
    }

    @Test
    public void testNormaliseTextNodesWithNoText() {
        document.appendElement("html").appendElement("body");
        document.normalise();
        assertEquals("", document.text());
        assertNotNull(document.body().text());
        assertEquals("", document.body().text());
    }

    @Test
    public void testNormaliseTextNodesWithMultipleTextNodes() {
        document.appendElement("html").appendElement("body").appendText("Text 1").appendText("Text 2").appendText("Text 3");
        document.normalise();
        assertEquals("", document.text());
        assertNotNull(document.body().text());
        assertEquals("Text 1 Text 2 Text 3", document.body().text());
    }

    @Test
    public void testNormaliseTextNodesWithNestedElements() {
        document.appendElement("html").appendElement("body").appendElement("p").appendText("Nested Text");
        document.normalise();
        assertEquals("", document.text());
        assertNotNull(document.body());
        assertNotNull(document.body().text());
        assertEquals("", document.body().text());
        assertNotNull(document.body().child(0));
        assertNotNull(document.body().child(0).text());
        assertEquals("Nested Text", document.body().child(0).text());
    }

    @Test
    public void testNormaliseTextNodesWithParentElementContainingMultipleChildElements() {
        document.appendElement("html").appendElement("body")
                .appendElement("p").appendText("Text 1").appendElement("a").appendText("Text 2");
        document.normalise();
        assertEquals("", document.text());
        assertNotNull(document.body());
        assertNotNull(document.body().text());
        assertEquals("", document.body().text());
        assertNotNull(document.body().child(0));
        assertNotNull(document.body().child(0).text());
        assertEquals("Text 1 Text 2", document.body().child(0).text());
    }

    @Test
    public void testOuterHtml() {
        String expectedHtml = "<html>\n <head></head>\n <body></body>\n</html>";
        assertEquals(expectedHtml, document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.text());
    }

    @Test
    public void testTextWithNestedElements() {
        document.appendElement("p").appendText("Text 1");
        document.appendElement("a").appendText("Text 2");
        document.text("New Text");
        assertEquals("New Text", document.text());
        assertNotNull(document.body().text());
        assertEquals("", document.body().text());
        assertNotNull(document.body().child(0));
        assertNotNull(document.body().child(0).text());
        assertEquals("New Text", document.body().child(0).text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        assertEquals(Entities.EscapeMode.base, document.outputSettings().escapeMode());
    }

    @Test
    public void testOutputSettingsSetEscapeMode() {
        document.outputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettingsSetCharset() {
        document.outputSettings().charset(Charset.forName("ISO-8859-1"));
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettingsSetCharsetByName() {
        document.outputSettings().charset("ISO-8859-1");
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        assertTrue(document.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsSetPrettyPrint() {
        document.outputSettings().prettyPrint(false);
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        assertEquals(1, document.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettingsSetIndentAmount() {
        document.outputSettings().indentAmount(2);
        assertEquals(2, document.outputSettings().indentAmount());
    }
}