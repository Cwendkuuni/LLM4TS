import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals("html", shell.tagName());
        assertEquals("http://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        Element headElement = document.createElement("head");
        document.appendChild(headElement);

        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        Element bodyElement = document.createElement("body");
        document.appendChild(bodyElement);

        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        Element titleElement = document.createElement("title");
        document.head().appendChild(titleElement);

        titleElement.text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testNormalise() {
        Element html = document.createElement("html");
        document.appendChild(html);

        Document normalised = document.normalise();
        assertNotNull(normalised);

        Element body = normalised.body();
        assertNotNull(body);

        Element head = normalised.head();
        assertNotNull(head);

        int textNodeCount = 0;
        for (Node child : normalised.childNodes) {
            if (child instanceof TextNode) {
                textNodeCount++;
            }
        }
        assertEquals(0, textNodeCount);
    }

    @Test
    public void testText() {
        Element body = document.createElement("body");
        document.appendChild(body);

        document.text("Test Text");
        String text = document.body().text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }
}