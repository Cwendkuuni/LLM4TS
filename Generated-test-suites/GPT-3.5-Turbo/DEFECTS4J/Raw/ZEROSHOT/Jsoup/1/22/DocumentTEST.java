import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testConstructor() {
        assertNotNull(document);
        assertEquals(Tag.valueOf("#root"), document.tag());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertNotNull(shell);
        assertEquals(Tag.valueOf("html"), shell.tag());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tag().getName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tag().getName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tag().getName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
        Element html = normalizedDocument.select("html").first();
        assertNotNull(html);
        Element head = normalizedDocument.head();
        assertNotNull(head);
        Element body = normalizedDocument.body();
        assertNotNull(body);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml); // No outer wrapper tag
    }

    @Test
    public void testText() {
        Document withText = document.text("Test Text");
        assertNotNull(withText);
        assertEquals("Test Text", document.body().text());
        assertEquals("", document.html()); // The document structure should not be modified
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}