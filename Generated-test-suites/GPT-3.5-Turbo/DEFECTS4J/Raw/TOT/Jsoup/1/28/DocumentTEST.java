import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {
    
    private Document document;

    @BeforeEach
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("html", shell.getElementsByTag("html").first().tagName());
        assertEquals("head", shell.head().tagName());
        assertEquals("body", shell.body().tagName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.head().appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        document.getElementsByTag("title").first().text("Updated Title");
        title = document.title();
        assertEquals("Updated Title", title);
    }

    @Test
    public void testInvalidTitle() {
        assertThrows(NullPointerException.class, () -> {
            document.title(null);
        });
    }

    @Test
    public void testNormalise() {
        document.createElement("h1").text("Heading 1");
        document.normalise();
        Element body = document.body();
        assertEquals(1, body.children().size());
        assertEquals("h1", body.child(0).tagName());
        assertEquals("Heading 1", body.child(0).text());
    }

    @Test
    public void testNormaliseWithExistingHeadAndBody() {
        document.createElement("h1").text("Heading 1");
        Element head = document.head();
        head.appendElement("meta").attr("charset", "UTF-8");
        document.normalise();
        Element body = document.body();
        assertEquals(2, body.children().size());
        assertEquals("h1", body.child(0).tagName());
        assertEquals("Heading 1", body.child(0).text());
        assertEquals("meta", body.child(1).tagName());
        assertEquals("charset", body.child(1).attr("charset"));
        assertEquals("UTF-8", body.child(1).attr("charset"));
    }

    @Test
    public void testOuterHtml() {
        document.createElement("p").text("Paragraph");
        String outerHtml = document.outerHtml();
        assertEquals("<p>Paragraph</p>", outerHtml);
    }

    @Test
    public void testText() {
        document.createElement("p").text("Paragraph");
        document.createElement("span").text("Span");
        Element body = document.body();
        assertEquals(2, body.children().size());
        document.text("Updated Text");
        assertEquals(1, body.children().size());
        assertEquals("Updated Text", body.text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}