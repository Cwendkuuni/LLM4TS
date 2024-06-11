import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    // Test case for creating a new Document
    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    // Test case for creating a shell Document
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();

        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    // Test case for accessing the head element
    @Test
    public void testHeadElement() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Assert.assertEquals("head", head.nodeName());
    }

    // Test case for accessing the body element
    @Test
    public void testBodyElement() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        Assert.assertEquals("body", body.nodeName());
    }

    // Test case for getting the title of the document
    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    // Test case for creating a new element
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element anchor = doc.createElement("a");
        Assert.assertEquals("a", anchor.nodeName());
        Assert.assertEquals("http://example.com", anchor.baseUri());
    }

    // Test case for normalizing the document
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element htmlEl = doc.getElementsByTag("html").first();

        doc.normalise();

        Element head = doc.head();
        Element body = doc.body();

        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals("head", head.nodeName());
        Assert.assertEquals("body", body.nodeName());
        Assert.assertTrue(htmlEl.parent().equals(doc));
    }

    // Test case for getting the outer HTML of the document
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        html.text("Test Text");

        String outerHtml = doc.outerHtml();
        Assert.assertEquals("Test Text", outerHtml);
    }

    // Test case for setting the text of the body of the document
    @Test
    public void testSetText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");

        Element body = doc.body();
        Assert.assertEquals("Test Text", body.text());
    }

    // Test case for getting the document's output settings
    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();

        Assert.assertNotNull(outputSettings);
        Assert.assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    }

    // Test case for setting the document's output settings
    @Test
    public void testSetOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doc.outputSettings(outputSettings);

        Assert.assertEquals(outputSettings, doc.outputSettings());
    }

    // Test case for getting the quirks mode of the document
    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();

        Assert.assertEquals(Document.QuirksMode.noQuirks, quirksMode); // Default value is noQuirks
    }

    // Test case for setting the quirks mode of the document
    @Test
    public void testSetQuirksMode() {
        Document doc = Document.createShell("http://example.com");
        doc.quirksMode(Document.QuirksMode.quirks);

        Assert.assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }

}