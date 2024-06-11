import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @After
    public void tearDown() {
        document = null;
    }

    @Test
    public void testCreateDocument() {
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
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
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");

        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
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

        document.head().appendElement("title").text("Page Title");

        title = document.title();
        assertEquals("Page Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);

        document.head().removeChild("-title");

        document.title(null);
        title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://www.example.com", element.baseUri());
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

        assertEquals(0, html.siblingIndex());
        assertEquals(1, head.siblingIndex());
        assertEquals(2, body.siblingIndex());

        Element p = document.createElement("p");
        p.text("Paragraph");

        document.prepend(p);
        document.normalise();

        Element newBody = document.body();
        assertNotNull(newBody);
        assertEquals(7, newBody.childNodeSize());
        assertEquals("p", newBody.childNode(0).nodeName());
        assertEquals("Paragraph", newBody.childNode(0).text());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertTrue(outerHtml.isEmpty());
    }

    @Test
    public void testText() {
        document.text("Sample Text");
        Element body = document.body();

        assertNotNull(body);
        assertEquals("Sample Text", body.text());

        document.text(null);
        assertEquals("", body.text());
    }

}