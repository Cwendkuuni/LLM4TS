import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        Element html = shell.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element headElement = document.head();
        assertNull(headElement);
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNull(bodyElement);
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Element div = document.createElement("div");
        div.text("Some text");
        document.appendChild(div);
        document.normalise();
        Element body = document.body();
        assertEquals("Some text", body.text());
    }

    @Test
    public void testText() {
        document.text("Some text");
        assertEquals("Some text", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(1, outputSettings.indentAmount());
    }
}