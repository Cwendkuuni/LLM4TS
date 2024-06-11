import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DocumentTest {
    @Test
    public void testDocumentConstructor() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertEquals("html", document.nodeName());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("Test", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        document.title("Test");
        Assert.assertEquals("Test", document.title());
    }

    @Test
    public void testNormalise() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        document.normalise();
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testcreateElement() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        Assert.assertEquals("a", document.createElement("a").nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("<head><title>Test</title></head><body></body>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        document.text("Example text");
        Assert.assertEquals("Example text", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertNotNull(document.outputSettings());
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }
}