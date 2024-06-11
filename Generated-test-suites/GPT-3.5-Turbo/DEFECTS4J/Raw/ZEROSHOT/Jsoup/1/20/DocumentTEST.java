import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleWithEmptyTitleElement() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleWithNonEmptyTitleElement() {
        Element titleElement = document.createElement("title");
        titleElement.text("Test Title");
        document.head().appendChild(titleElement);

        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleWithMissingTitleElement() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testTextNode() {
        Element textNode = new TextNode("Test Text", "");
        assertNotNull(textNode);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}