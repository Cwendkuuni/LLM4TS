import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }

    @Test
    public void testDocumentConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("https://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");

        assertEquals("https://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("html", shell.body().parent().nodeName());
    }

    @Test
    public void testGetHead() {
        Element head = document.head();

        assertNotNull(head);
        assertEquals("head", head.nodeName());
        assertEquals(document, head.parent());
    }

    @Test
    public void testGetBody() {
        Element body = document.body();

        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals(document, body.parent());
    }

    @Test
    public void testGetTitleWhenTitleExists() {
        document.head().appendElement("title").text("Test Title");

        String title = document.title();

        assertEquals("Test Title", title);
    }

    @Test
    public void testGetTitleWhenTitleDoesNotExist() {
        String title = document.title();

        assertEquals("", title);
    }

    @Test
    public void testSetTitleWhenTitleElementExists() {
        document.head().appendElement("title").text("Old Title");

        document.title("New Title");

        assertEquals("New Title", document.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitleWhenTitleElementDoesNotExist() {
        document.title("New Title");

        assertEquals("New Title", document.getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        assertEquals("div", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.body().prependChild(new TextNode("Some random text", ""));
        document.normalise();

        assertEquals("Some random text", document.body().text());
        assertEquals(0, document.head().childNodes().size());
        assertEquals(2, document.html().childNodes().size());
        assertEquals("head", document.html().child(0).nodeName());
        assertEquals("body", document.html().child(1).nodeName());
    }

    @Test
    public void testOuterHtml() {
        Element div = document.createElement("div");
        div.attr("id", "test");
        document.body().appendChild(div);

        String outerHtml = document.outerHtml();

        assertEquals("<div id=\"test\"></div>", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Some text");

        assertEquals("Some text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}