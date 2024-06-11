import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertNotNull(shell.getElementsByTag("html").first());
        assertEquals("head", shell.getElementsByTag("html").first().getElementsByTag("head").first().nodeName());
        assertEquals("body", shell.getElementsByTag("html").first().getElementsByTag("body").first().nodeName());
    }

    @Test
    public void testDocumentHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testDocumentBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testDocumentTitle() {
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertEquals("title", document.getElementsByTag("head").first().getElementsByTag("title").first().nodeName());
        assertEquals("Test Title", document.getElementsByTag("head").first().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("", element.text());
        assertEquals("", element.html());
        assertEquals("", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalised = document.normalise();
        assertNotNull(normalised);
        assertNotNull(normalised.getElementsByTag("html").first());
        assertNotNull(normalised.head());
        assertNotNull(normalised.body());
        assertEquals("html", normalised.getElementsByTag("html").first().nodeName());
        assertEquals("head", normalised.head().nodeName());
        assertEquals("body", normalised.body().nodeName());
    }

    @Test
    public void testOuterHtml() {
        String html = document.outerHtml();
        assertNotNull(html);
        assertEquals("", html);
    }

    @Test
    public void testSetText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
        assertEquals(" ", document.body().childNodes.get(0).outerHtml());
        assertEquals("Test Text", document.body().childNodes.get(1).outerHtml());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}