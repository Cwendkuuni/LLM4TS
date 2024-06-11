import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    // Test case for the createShell method
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("html", doc.getElementsByTag("html").first().nodeName());
        assertEquals("head", doc.getElementsByTag("head").first().nodeName());
        assertEquals("body", doc.getElementsByTag("body").first().nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    // Test case for the head method
    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    // Test case for the body method
    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    // Test case for the title method
    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    // Test case for the createElement method
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    // Test case for the normalise method
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);

        NodeList nodes = doc.childNodes();
        for (Node node : nodes) {
            if (node instanceof TextNode) {
                assertTrue(body.childNodes().contains(node));
            }
        }
    }

    // Test case for the outerHtml method
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        String outerHtml = doc.outerHtml();
        assertEquals("", outerHtml);
    }

    // Test case for the text method
    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");

        Element body = doc.body();
        assertNotNull(body);

        String text = body.text();
        assertEquals("Test Text", text);
    }
}