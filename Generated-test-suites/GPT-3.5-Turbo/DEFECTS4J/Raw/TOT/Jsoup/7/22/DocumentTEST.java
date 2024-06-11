import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals(Tag.valueOf("html"), shell.tagName());
        assertNotNull(shell.head());
        assertEquals(Tag.valueOf("head"), shell.head().tagName());
        assertNotNull(shell.body());
        assertEquals(Tag.valueOf("body"), shell.body().tagName());
    }

    @Test
    public void testHead() {
        assertNull(document.head());

        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals(Tag.valueOf("head"), document.head().tagName());
    }

    @Test
    public void testBody() {
        assertNull(document.body());

        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals(Tag.valueOf("body"), document.body().tagName());
    }

    @Test
    public void testTitleWithoutTitleElement() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithEmptyTitleElement() {
        document.head().appendElement("title");
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithNonEmptyTitleElement() {
        document.head().appendElement("title").text("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleWithNullTitle() {
        document.title(null);
    }

    @Test
    public void testSetTitleWithNonEmptyTitleElement() {
        document.head().appendElement("title").text("Old Title");
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testSetTitleWithoutTitleElement() {
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
        assertNotNull(document.head());
        assertNotNull(document.head().getElementsByTag("title"));
        assertEquals(1, document.head().getElementsByTag("title").size());
        assertEquals("New Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Document.Element element = document.createElement(tagName);
        assertNotNull(element);
        assertEquals(Tag.valueOf(tagName), element.tag());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        Element htmlElement = document.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        Element headElement = document.getElementsByTag("head").first();
        assertNotNull(headElement);
        Element bodyElement = document.getElementsByTag("body").first();
        assertNotNull(bodyElement);

        // Test moving text nodes to body
        Element textElement = new Element(Tag.valueOf("text")).text("Hello World");
        document.prependChild(textElement);
        document.normalise();
        assertEquals(0, document.childNodesSize());
        assertEquals(2, bodyElement.childNodesSize());
        assertEquals(textElement, bodyElement.childNodes.get(0));
        assertNotNull(bodyElement.childNodes.get(1));
        assertTrue(bodyElement.childNodes.get(1) instanceof TextNode);
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());

        document.text("Hello World");
        assertEquals("Hello World", document.outerHtml());
    }

    @Test
    public void testText() {
        assertEquals(document, document.text("Hello World"));
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());

        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}