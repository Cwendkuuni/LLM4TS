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
    public void testConstructor() {
        assertNotNull(document);
        assertTrue(document.baseUri().equals("https://example.com"));
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertEquals(shell.head().tagName(), "head");
        assertEquals(shell.body().tagName(), "body");
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
        
        document.createElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals(head.tagName(), "head");
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
        
        document.createElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals(body.tagName(), "body");
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals(title, "");
        
        document.title("Test Title");
        title = document.title();
        assertEquals(title, "Test Title");
        
        document.title(null);
        title = document.title();
        assertEquals(title, "");
    }

    @Test
    public void testNormalise() {
        document.normalise();
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(outputSettings.escapeMode(), org.jsoup.nodes.Entities.EscapeMode.base);
        assertEquals(outputSettings.charset().displayName(), "UTF-8");
        assertEquals(outputSettings.prettyPrint(), true);
        assertEquals(outputSettings.indentAmount(), 1);

        outputSettings.escapeMode(org.jsoup.nodes.Entities.EscapeMode.extended);
        assertEquals(outputSettings.escapeMode(), org.jsoup.nodes.Entities.EscapeMode.extended);

        outputSettings.charset("ISO-8859-1");
        assertEquals(outputSettings.charset().displayName(), "ISO-8859-1");

        outputSettings.prettyPrint(false);
        assertEquals(outputSettings.prettyPrint(), false);

        outputSettings.indentAmount(2);
        assertEquals(outputSettings.indentAmount(), 2);
    }

    @Test
    public void testText() {
        Element body = document.body();
        assertNull(body.text());

        document.text("Test Body Text");
        assertEquals(body.text(), "Test Body Text");

        document.text(null);
        assertNull(body.text());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.isEmpty());

        String html = "<html><head></head><body></body></html>";
        document = Jsoup.parse(html);
        outerHtml = document.outerHtml();
        assertEquals(html, outerHtml);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(element.tagName(), "div");
        assertEquals(element.baseUri(), document.baseUri());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals(nodeName, "#document");
    }

    @Test
    public void testNormaliseTextNodes() {
        Element element = document.createElement("div");
        Element child1 = document.createElement("p");
        Element child2 = document.createElement("span");
        child1.appendText("Text Node 1");
        child2.appendText("Text Node 2");
        element.appendChild(child1);
        element.appendChild(child2);

        document.normaliseTextNodes(element);

        assertEquals(element.childNodes().size(), 3);
        assertEquals(element.childNodes().get(0).getClass(), org.jsoup.nodes.TextNode.class);
        assertEquals(element.childNodes().get(0).outerHtml().trim(), "Text Node 1");
        assertEquals(element.childNodes().get(1).getClass(), org.jsoup.nodes.TextNode.class);
        assertEquals(element.childNodes().get(1).outerHtml().trim(), "Text Node 2");
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element html = document.createElement("html");
        Element head = document.createElement("head");
        html.appendChild(head);
        Element body = document.createElement("body");
        html.appendChild(body);
        document.appendChild(html);

        Element found = document.findFirstElementByTagName("head", document);
        assertNotNull(found);
        assertEquals(found, head);

        found = document.findFirstElementByTagName("body", document);
        assertNotNull(found);
        assertEquals(found, body);

        found = document.findFirstElementByTagName("head", body);
        assertNull(found);
    }

}