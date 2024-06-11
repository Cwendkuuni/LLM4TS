import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;
    private String baseUri;

    @Before
    public void setUp() {
        baseUri = "https://www.example.com";
        document = new Document(baseUri);
    }

    @After
    public void tearDown() {
        document = null;
        baseUri = null;
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(baseUri);

        assertNotNull(shell);
        assertEquals(baseUri, shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().tagName());
        assertEquals("head", shell.head().tagName());
        assertEquals("html", shell.body().parent().tagName());
        assertEquals("body", shell.body().tagName());
    }

    @Test
    public void testHead() {
        Element head = document.head();

        assertNull(head);

        document.createElement("head");
        head = document.head();

        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();

        assertNull(body);

        document.createElement("body");
        body = document.body();

        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();

        assertTrue(title.isEmpty());

        document.head().appendElement("title").text("Test Title");
        title = document.title();

        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");

        assertEquals("New Title", document.title());

        document.title(null);

        assertTrue(document.title().isEmpty());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(baseUri, element.baseUri());
        assertNull(element.parent());
    }

    @Test
    public void testNormalise() {
        document.appendElement("html");
        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());

        document.getElementsByTag("html").first().remove();
        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    // Add more test cases for other methods

}