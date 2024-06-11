
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import java.util.List;
import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.outerHtml());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Existing Title");
        String title = document.title();
        assertEquals("Existing Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testTitleWithNullTitle() {
        try {
            document.title(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.getElementsByTag("html").first());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertTrue(document.select("body").text().isEmpty());
    }

    @Test
    public void testNormaliseWithExistingElements() {
        document.head().appendElement("title").text("Existing Title");
        document.appendElement("p").text("Sample Paragraph");
        document.normalise();
        Element head = document.head();
        Element body = document.body();
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("Existing Title", head.getElementsByTag("title").text());
        assertEquals("Sample Paragraph", body.text());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Sample Text");
        assertEquals("Sample Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}
