import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;
    private String baseUri;

    @Before
    public void setUp() {
        baseUri = "https://www.example.com";
        document = new Document(baseUri);
    }

    @Test
    public void testConstructor() {
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("#root", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(baseUri);
        assertNotNull(shell);
        assertEquals(baseUri, shell.baseUri());
        assertEquals("html", shell.nodeName());
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
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertNotNull(title);
        assertEquals("Test Title", title);

        document.title(null);
        title = document.title();
        assertNotNull(title);
        assertEquals("", title);
    }

    @Test
    public void testAppendElement() {
        String tagName = "div";
        Element element = document.appendElement(tagName);
        assertNotNull(element);
        assertEquals(tagName, element.tagName());
        assertTrue(element.parent() == document);
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
        assertEquals("html", normalizedDoc.nodeName());
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        String tagName = "div";
        Element element = document.appendElement(tagName);
        assertNotNull(element);

        TextNode textNode1 = new TextNode("Hello", "");
        TextNode textNode2 = new TextNode("World", "");
        element.appendChild(textNode1);
        element.appendChild(textNode2);

        document.normaliseTextNodes(element);
        assertEquals(0, element.childNodes().size());
        assertTrue(document.body().childNodes().contains(textNode1));
        assertTrue(document.body().childNodes().contains(textNode2));
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        String text = "Sample text";
        document.text(text);
        assertTrue(document.body().text().equals(text));
    }

    @Test
    public void testOutputSettings() {
        assertNotNull(document.outputSettings());
        assertEquals("base", document.outputSettings().escapeMode().name());
        assertEquals("UTF-8", document.outputSettings().charset().name());
        assertTrue(document.outputSettings().prettyPrint());

        document.outputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals("extended", document.outputSettings().escapeMode().name());

        document.outputSettings().charset(Charset.forName("ISO-8859-1"));
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());

        document.outputSettings().charset("UTF-16");
        assertEquals("UTF-16", document.outputSettings().charset().name());

        document.outputSettings().prettyPrint(false);
        assertFalse(document.outputSettings().prettyPrint());

        document.outputSettings().indentAmount(4);
        assertEquals(4, document.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettingsEncoder() {
        assertNotNull(document.outputSettings().encoder());
    }

    @Test
    public void testOutputSettingsCharsetException() {
        document.outputSettings().charset(Charset.forName("InvalidCharset123"));
        assertNotNull(document.outputSettings().charset());
    }
}