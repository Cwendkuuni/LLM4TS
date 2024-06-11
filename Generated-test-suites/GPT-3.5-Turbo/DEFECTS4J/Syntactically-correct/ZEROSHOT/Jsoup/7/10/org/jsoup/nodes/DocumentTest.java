import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testDocumentInitialization() {
        Document document = new Document("http://example.com");
        Assert.assertEquals(Tag.valueOf("#root"), document.tag());
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("html", document.tag().getName());
        Assert.assertEquals("head", document.head().tag().getName());
        Assert.assertEquals("body", document.body().tag().getName());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Assert.assertNull(document.head());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Assert.assertNull(document.body());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("", document.title());

        document.appendElement("title").text("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("New Title");
        Assert.assertEquals("New Title", document.title());

        document.title("Updated Title");
        Assert.assertEquals("Updated Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("a", document.createElement("a").tag().getName());
        Assert.assertEquals("div", document.createElement("div").tag().getName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.normalise();
        // TODO: Add assertion for normalise method
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Assert.assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test text");
        // TODO: Add assertion for text method
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Assert.assertNotNull(document.outputSettings());
        // TODO: Add assertions for output settings methods
    }
}