import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        // Typical use case
        Document document = new Document("http://example.com");
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testDocumentCreateShell() {
        // Typical use case
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("html", document.head().parent().nodeName());
        Assert.assertEquals("html", document.body().parent().nodeName());
    }

    @Test
    public void testDocumentHead() {
        // Typical use case
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("head", document.head().nodeName());

        // Edge case: No head element
        document.head().remove();
        Assert.assertNull(document.head());
    }

    @Test
    public void testDocumentBody() {
        // Typical use case
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("body", document.body().nodeName());

        // Edge case: No body element
        document.body().remove();
        Assert.assertNull(document.body());
    }

    @Test
    public void testDocumentTitle() {
        // Typical use case: Title element exists
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());

        // Typical use case: No title element
        document.getElementsByTag("title").remove();
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testDocumentSetTitle() {
        // Typical use case: Title element exists
        Document document = Document.createShell("http://example.com");
        document.setTitle("Test Title");
        Assert.assertEquals("Test Title", document.title());

        // Typical use case: No title element
        document.getElementsByTag("title").remove();
        document.setTitle("New Title");
        Assert.assertEquals("New Title", document.title());

        // Error scenario: No title provided
        try {
            document.setTitle(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Title must not be null", e.getMessage());
        }
    }

    @Test
    public void testDocumentCreateElement() {
        // Typical use case
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("a", document.createElement("a").nodeName());

        // Edge case: Empty tag name
        Assert.assertEquals("", document.createElement("").nodeName());
    }

    @Test
    public void testDocumentNormalise() {
        // Typical use case: Normalise called on document without html, head, and body elements
        Document document = new Document("http://example.com");
        document.normalise();
        Assert.assertEquals("html", document.head().parent().nodeName());
        Assert.assertEquals("html", document.body().parent().nodeName());

        // Typical use case: Normalise called on document with existing html, head, and body elements
        Document document2 = Document.createShell("http://example.com");
        document2.normalise();
        Assert.assertEquals("head", document2.head().nodeName());
        Assert.assertEquals("body", document2.body().nodeName());
    }

    @Test
    public void testDocumentOuterHtml() {
        // Typical use case
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testDocumentText() {
        // Typical use case
        Document document = Document.createShell("http://example.com");
        document.text("Test text");
        Assert.assertEquals("Test text", document.body().text());

        // Error scenario: Null text provided
        try {
            document.text(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Text must not be null", e.getMessage());
        }
    }
}