import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testDocumentConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertEquals("html", shell.getElementsByTag("html").first().nodeName());
        assertEquals("head", shell.head().nodeName());
        assertEquals("body", shell.body().nodeName());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        assertEquals("a", document.createElement("a").nodeName());
        assertEquals("div", document.createElement("div").nodeName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertEquals("html", document.getElementsByTag("html").first().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testSetText() {
        document.text("This is a test");
        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}