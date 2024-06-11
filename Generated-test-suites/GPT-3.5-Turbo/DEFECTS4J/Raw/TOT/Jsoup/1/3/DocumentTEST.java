import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());

        doc.title("Title");
        assertEquals("Title", doc.title());

        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Existing Title");
        doc.title("New Title");

        assertEquals("New Title", doc.title());
        assertEquals("New Title", doc.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testTitleWithNullTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Existing Title");

        doc.select("title").remove();
        doc.title("New Title");

        assertEquals("New Title", doc.title());
        assertEquals("New Title", doc.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");

        doc.select("head").remove();
        doc.select("body").remove();
        doc.normalise();

        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());

        doc.select("head").remove();
        doc.select("body").append("<p>Text</p>");
        doc.normalise();

        assertEquals("", doc.head().html());
        assertEquals("<p>Text</p>", doc.body().html());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Title");
        doc.body().appendElement("div").text("Text");

        assertEquals(doc.html(), doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Text");

        assertEquals("Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

}