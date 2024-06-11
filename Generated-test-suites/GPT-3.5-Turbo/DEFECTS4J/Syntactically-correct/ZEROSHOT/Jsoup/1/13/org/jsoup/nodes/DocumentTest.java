
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("#root", document.tagName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("html", document.tagName());
        assertNotNull(document.getElementsByTag("head"));
        assertNotNull(document.getElementsByTag("body"));
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleInvalid() {
        Document document = new Document("http://example.com");
        try {
            document.title(null);
            fail("Should throw exception for null title");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testcreateElement() {
        Document document = new Document("http://example.com");
        assertNotNull(document.createElement("div"));
        assertEquals("div", document.createElement("div").tagName());
        assertEquals("http://example.com", document.createElement("div").baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.createElement("div").text("Test").appendTo(document.body());
        document.normalise();
        assertEquals("Test", document.body().text());

        document.normalise();
        assertEquals("Test", document.body().text());

        document.normalise();
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.createElement("div").text("Test").appendTo(document.body());
        assertEquals("<div>Test</div>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test");
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }
}
