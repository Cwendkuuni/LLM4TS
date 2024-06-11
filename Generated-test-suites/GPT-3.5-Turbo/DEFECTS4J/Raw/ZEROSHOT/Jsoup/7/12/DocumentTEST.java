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
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
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
        assertNotNull(document.getElementsByTag("title").first());
    }

    @Test
    public void testCreateElement() {
        assertEquals("div", document.createElement("div").tagName());
    }

    @Test
    public void testNormalise() {
        Document normalisedDocument = document.normalise();
        assertNotNull(normalisedDocument);
        assertNotNull(normalisedDocument.getElementsByTag("html").first());
        assertNotNull(normalisedDocument.head());
        assertNotNull(normalisedDocument.body());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testEscapeMode() {
        assertEquals(org.jsoup.nodes.Entities.EscapeMode.base, document.outputSettings().escapeMode());
        document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.extended);
        assertEquals(org.jsoup.nodes.Entities.EscapeMode.extended, document.outputSettings().escapeMode());
    }

    @Test
    public void testCharset() {
        assertEquals(java.nio.charset.Charset.forName("UTF-8"), document.outputSettings().charset());
        document.outputSettings().charset(java.nio.charset.Charset.forName("ISO-8859-1"));
        assertEquals(java.nio.charset.Charset.forName("ISO-8859-1"), document.outputSettings().charset());
    }

    @Test
    public void testPrettyPrint() {
        assertTrue(document.outputSettings().prettyPrint());
        document.outputSettings().prettyPrint(false);
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        assertEquals(1, document.outputSettings().indentAmount());
        document.outputSettings().indentAmount(4);
        assertEquals(4, document.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettings() {
        assertNotNull(document.outputSettings());
    }
}