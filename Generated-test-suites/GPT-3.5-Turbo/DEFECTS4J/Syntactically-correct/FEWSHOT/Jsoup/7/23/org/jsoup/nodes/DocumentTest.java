import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");

        assertNotNull(shell);
        assertEquals("#root", shell.nodeName());
        assertEquals("http://example.com", shell.baseUri());

        Element html = shell.head().parent();
        assertNotNull(html);
        assertEquals("html", html.nodeName());

        assertNotNull(html.head());
        assertEquals("head", html.head().nodeName());

        assertNotNull(html.body());
        assertEquals("body", html.body().nodeName());
    }

    @Test
    public void testHead() {
        Element head = document.head();

        assertNull(head);

        document.appendElement("head");

        head = document.head();

        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.head();

        assertNull(body);

        document.appendElement("body");

        body = document.body();

        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();

        assertEquals("", title);

        Element head = document.appendElement("head");
        head.appendElement("title").text("Page Title");

        title = document.title();

        assertEquals("Page Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Page Title");

        String title = document.title();

        assertEquals("Page Title", title);
    }

    @Test
    public void testNormalise() {
        document.appendElement("head");
        document.appendElement("body");
        document.appendElement("div").text("Text");

        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("Text", document.body().text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element htmlElement = document.appendElement("html");
        Element headElement = htmlElement.appendElement("head");
        Element bodyElement = htmlElement.appendElement("body");

        headElement.appendElement("title").text("Page Title");
        bodyElement.appendChild(Document.createTextNode("Text Node 1"));

        Document.TextNode textNode2 = Document.createTextNode("Text Node 2");
        bodyElement.appendChild(textNode2);

        document.normalise();

        assertEquals(2, bodyElement.childNodes().size());
        assertEquals("Text Node 2", bodyElement.childNode(0).outerHtml());
        assertEquals("Text Node 1", bodyElement.childNode(1).outerHtml());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("div").text("Text");

        String outerHtml = document.outerHtml();

        assertEquals("<div>Text</div>", outerHtml);
    }

    @Test
    public void testText() {
        document.appendElement("body").text("Text");

        assertEquals("Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().displayName());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(EscapeMode.extended);
        outputSettings.charset("ISO-8859-1");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);

        assertEquals(EscapeMode.extended, outputSettings.escapeMode());
        assertEquals("ISO-8859-1", outputSettings.charset().displayName());
        assertEquals(false, outputSettings.prettyPrint());
        assertEquals(2, outputSettings.indentAmount());
    }
}