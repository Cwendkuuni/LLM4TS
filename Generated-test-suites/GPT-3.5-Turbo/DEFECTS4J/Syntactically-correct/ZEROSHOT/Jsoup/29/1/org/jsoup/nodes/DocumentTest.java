import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        Assert.assertNotNull(html);

        Element head = doc.getElementsByTag("head").first();
        Assert.assertNotNull(head);

        Element body = doc.getElementsByTag("body").first();
        Assert.assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.appendElement("p").text("Test paragraph");
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <p>Test paragraph</p>\n </body>\n</html>", doc.normalise().outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test text");
        Element body = doc.body();
        Assert.assertEquals("Test text", body.text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.appendElement("div").text("Test div");
        Assert.assertEquals("<div>Test div</div>", doc.outerHtml());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://example.com");
        Document clone = doc.clone();
        Assert.assertNotSame(doc, clone);
        Assert.assertEquals(doc.outerHtml(), clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);
        Assert.assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
        doc.quirksMode(quirksMode);
        Assert.assertEquals(quirksMode, doc.quirksMode());
    }
}