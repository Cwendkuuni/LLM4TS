import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Test;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    private Document createDocument() {
        return new Document("");
    }

    @Test
    public void testConstructor() {
        Document document = createDocument();
        assertEquals("#root", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
        assertEquals("", document.title());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("");
        Element html = document.child(0);
        Element head = document.getElementsByTag("head").first();
        Element body = document.getElementsByTag("body").first();
        assertEquals("html", html.tagName());
        assertEquals("head", head.tagName());
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHead() {
        Document document = createDocument();
        assertNull(document.head());
    }

    @Test
    public void testBody() {
        Document document = createDocument();
        assertNull(document.body());
    }

    @Test
    public void testTitle() {
        Document document = createDocument();
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testNormalise() {
        Document document = createDocument();
        document.normalise();
        // Test if head and body elements are created
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = createDocument();
        document.normaliseTextNodes(document);

        List<Node> textNodes = new ArrayList<>();
        for (Node node: document.childNodes) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        boolean allTextNodesMoved = true;
        for (Node node: textNodes) {
            if (!(document.body().childNodes.contains(node))) {
                allTextNodesMoved = false;
                break;
            }
        }

        assertTrue(allTextNodesMoved);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = createDocument();

        Element element = new Element(Tag.valueOf("div"), "");
        document.appendChild(element);

        Element foundElement = document.findFirstElementByTagName("div", document);
        assertEquals(element, foundElement);
    }

    @Test
    public void testOuterHtml() {
        Document document = createDocument();
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = createDocument();
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document document = createDocument();
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}