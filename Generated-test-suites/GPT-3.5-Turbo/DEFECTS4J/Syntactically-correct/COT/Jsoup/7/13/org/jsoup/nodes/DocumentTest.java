
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertTrue(doc.getElementsByTag("html").size() > 0);
        Assert.assertTrue(doc.getElementsByTag("head").size() > 0);
        Assert.assertTrue(doc.getElementsByTag("body").size() > 0);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Assert.assertNull(doc.head());
        doc.createElement("head");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Assert.assertNull(doc.body());
        doc.createElement("body");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("", doc.title());
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.createElement("title");
        doc.title("Test Title");
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertNotNull(titleEl);
        Assert.assertEquals("title", titleEl.nodeName());
        Assert.assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.createElement("body");
        doc.text("Test Text");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
        Assert.assertEquals("Test Text", body.text());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.createElement("html");
        doc.createElement("head");
        doc.createElement("body");
        doc.normalise();
        Assert.assertEquals(1, doc.getElementsByTag("html").size());
        Assert.assertEquals(1, doc.getElementsByTag("head").size());
        Assert.assertEquals(1, doc.getElementsByTag("body").size());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.createElement("p").appendText("Test Paragraph");
        Assert.assertEquals("<p>Test Paragraph</p>", doc.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        Assert.assertNotNull(settings);
        Assert.assertEquals(Document.OutputSettings.EscapeMode.base, settings.escapeMode());
        Assert.assertEquals("UTF-8", settings.charset().displayName());
        Assert.assertTrue(settings.prettyPrint());
        Assert.assertEquals(1, settings.indentAmount());
    }
}

