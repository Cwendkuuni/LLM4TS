
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }

    @Test
    public void testDocumentConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://www.example.com");
        assertNotNull(shell);
        assertEquals("http://www.example.com", shell.baseUri());
        assertEquals("html", shell.nodeName());

        Element head = shell.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());

        Element body = shell.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        String title = "Test Title";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Element element = document.createElement(tagName);
        assertNotNull(element);
        assertEquals(tagName, element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        assertEquals(3, normalizedDocument.childNodeSize());

        Element html = normalizedDocument.getChildNodes().get(0);
        assertNotNull(html);
        assertEquals("html", html.nodeName());

        Element head = html.getChildNodes().get(0);
        assertNotNull(head);
        assertEquals("head", head.nodeName());

        Element body = html.getChildNodes().get(2);
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testOuterHtml() {
        String html = document.outerHtml();
        assertEquals("", html);
    }

    @Test
    public void testText() {
        String text = "Test text";
        document.text(text);
        Element body = document.body();
        assertNotNull(body);
        assertEquals(text, body.text());
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
        assertEquals(Document.OutputSettings.escapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        assertEquals(outputSettings, outputSettings.escapeMode(Document.OutputSettings.escapeMode.extended));
        assertEquals(Document.OutputSettings.escapeMode.extended, outputSettings.escapeMode());

        assertEquals(outputSettings, outputSettings.charset("US-ASCII"));
        assertEquals("US-ASCII", outputSettings.charset().name());

        assertEquals(outputSettings, outputSettings.prettyPrint(false));
        assertFalse(outputSettings.prettyPrint());

        assertEquals(outputSettings, outputSettings.indentAmount(2));
        assertEquals(2, outputSettings.indentAmount());
    }
}
