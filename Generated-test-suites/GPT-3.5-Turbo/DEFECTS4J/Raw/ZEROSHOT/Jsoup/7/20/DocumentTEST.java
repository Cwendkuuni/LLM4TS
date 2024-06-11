import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");

        assertNotNull(doc);
        assertEquals(Tag.html, doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();

        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();

        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitleWithEmptyDocument() {
        String title = document.title();

        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Example Title");
        String title = document.title();

        assertEquals("Example Title", title);
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        document.appendElement("head").appendElement("title").text("Existing Title");
        document.title("New Title");
        String title = document.title();

        assertEquals("New Title", title);
    }

    @Test
    public void testSetTitleWithNoExistingTitleElement() {
        document.title("New Title");
        String title = document.title();

        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        document.prependElement("div");
        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals(1, document.body().children().size());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testNormaliseWithExistingHtmlElement() {
        document.appendElement("html").prependElement("div");
        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals(1, document.body().children().size());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();

        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Example Text");
        String text = document.text();

        assertEquals("Example Text", text);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();

        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}