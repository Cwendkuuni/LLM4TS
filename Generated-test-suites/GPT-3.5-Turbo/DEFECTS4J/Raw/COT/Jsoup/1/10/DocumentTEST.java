
import org.jsoup.Jsoup;
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
    public void testDocumentConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("http://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("head", shell.head().tagName());
        assertEquals("body", shell.body().tagName());
    }

    @Test
    public void testHead() {
        assertNull(document.head());

        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        assertNull(document.body());

        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
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

        document.head().empty();
        document.title("Another Title");
        assertEquals("Another Title", document.title());
    }

    @Test
    public void testCreateElement() {
        document.appendElement("div");
        assertNotNull(document.createElement("div"));
        assertEquals("div", document.createElement("div").tagName());
        assertEquals("http://example.com", document.createElement("div").baseUri());
    }

    @Test
    public void testNormalise() {
        Document emptyDoc = Document.createShell("http://example.com");
        assertEquals(" ", emptyDoc.normalise().body().text());

        Document doc = Document.createShell("http://example.com");
        doc.appendElement("html").prependElement("head");
        doc.appendElement("html").appendElement("body");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("div").text("test");
        assertEquals("<div>test</div>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.appendElement("body").text("test");
        assertEquals("test", document.text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}
