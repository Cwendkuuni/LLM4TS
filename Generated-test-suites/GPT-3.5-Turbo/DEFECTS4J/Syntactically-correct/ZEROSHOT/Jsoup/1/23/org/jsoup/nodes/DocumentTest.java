
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class DocumentTest {

    private Document document;
    private String baseUri = "http://example.com";

    @Before
    public void setUp() {
        document = new Document(baseUri);
    }

    @Test
    public void testConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(baseUri);
        assertNotNull(shell);
        assertEquals("html", shell.getElementsByTag("html").first().tagName());
        assertEquals("head", shell.head().tagName());
        assertEquals("body", shell.body().tagName());
        assertEquals("", shell.title());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());

        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
        assertEquals("html", normalizedDoc.getElementsByTag("html").first().tagName());
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Test text";
        document.text(text);
        assertEquals(text, document.body().text());
    }
}
