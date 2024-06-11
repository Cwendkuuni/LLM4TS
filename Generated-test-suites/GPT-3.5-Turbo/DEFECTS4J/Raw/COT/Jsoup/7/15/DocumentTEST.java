import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.Assert;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Document normalisedDoc = document.normalise();
        Assert.assertEquals(document, normalisedDoc);
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("test text");
        Assert.assertEquals("test text", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);
        Assert.assertEquals("UTF-8", outputSettings.charset().displayName());
    }
}