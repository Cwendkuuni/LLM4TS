import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element headElement = document.head();
        assertNotNull(headElement);
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");

        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        Element updatedBodyElement = document.text("Test Text");

        assertSame(bodyElement, updatedBodyElement);
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}