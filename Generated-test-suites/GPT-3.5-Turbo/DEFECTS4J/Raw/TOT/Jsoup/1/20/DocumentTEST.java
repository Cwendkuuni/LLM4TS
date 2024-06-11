import org.jsoup.parser.Tag;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document("http://example.com");
    }

    @AfterEach
    void tearDown() {
        document = null;
    }

    @Test
    void testDocumentCreation() {
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("http://example.com", document.baseUri());
        assertNull(document.parent());
    }

    @Test
    void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("http://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().tagName());
        assertEquals("body", shell.body().tagName());
    }

    @Test
    void testHead() {
        assertNotNull(document.head());
        assertNull(document.head().parent());
        assertEquals("head", document.head().tagName());
    }

    @Test
    void testBody() {
        assertNotNull(document.body());
        assertNull(document.body().parent());
        assertEquals("body", document.body().tagName());
    }

    @Test
    void testTitleWithExistingTitleElement() {
        String title = "Test Title";
        document.head().appendElement("title").text(title);
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    void testTitleWithoutExistingTitleElement() {
        String title = "Test Title";
        document.title(title);
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(title, titleElement.text());
    }

    @Test
    void testTitleWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> document.title(null));
    }

    @Test
    void testTitleElementTextWithExistingTitleElement() {
        String title = "Test Title";
        document.head().appendElement("title").text(title);
        document.title(title);
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(title, titleElement.text());
    }

    @Test
    void testTitleElementTextWithoutExistingTitleElement() {
        String title = "Test Title";
        document.title(title);
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(title, titleElement.text());
    }

    @Test
    void testTitleElementTextWithNullTitle() {
        document.head().appendElement("title");
        assertNull(document.title());
    }

    @Test
    void testTitleElementTextWithBlankTitle() {
        String title = "";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    void testTitleElementTextWithWhitespaceTitle() {
        String title = "   ";
        document.title(title);
        assertEquals("", document.title());
    }

    @Test
    void testCreateElement() {
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertNull(element.parent());
    }

    @Test
    void testNormalise() {
        Element body = document.createShell("http://example.com").body();
        Element div1 = body.appendElement("div");
        div1.text("Hello");
        Element div2 = body.appendElement("div");
        div2.text("World");
        Element p = div2.appendElement("p");
        p.text("Keep calm");
        Element span = body.appendElement("span");
        span.appendElement("em").text("this is");
        span.appendElement("strong").text(" a test");
        document.normalise();

        List<Node> bodyChildren = document.body().childNodes();
        assertEquals(4, bodyChildren.size());

        assertTrue(bodyChildren.get(0) instanceof Element);
        assertEquals("div", ((Element) bodyChildren.get(0)).tagName());
        assertTrue(bodyChildren.get(1) instanceof Element);
        assertEquals("div", ((Element) bodyChildren.get(1)).tagName());
        assertTrue(bodyChildren.get(2) instanceof TextNode);
        assertTrue(bodyChildren.get(3) instanceof Element);
        assertEquals("span", ((Element) bodyChildren.get(3)).tagName());

        Element spanElement = (Element) bodyChildren.get(3);
        List<Node> spanChildren = spanElement.childNodes();
        assertEquals(2, spanChildren.size());

        assertTrue(spanChildren.get(0) instanceof Element);
        assertEquals("em", ((Element) spanChildren.get(0)).tagName());
        assertTrue(spanChildren.get(1) instanceof Element);
        assertEquals("strong", ((Element) spanChildren.get(1)).tagName());
    }

    @Test
    void testOuterHtml() {
        String html = "<html><head></head><body></body></html>";
        assertEquals(html, document.outerHtml());
    }

    @Test
    void testText() {
        String text = "Test Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

}