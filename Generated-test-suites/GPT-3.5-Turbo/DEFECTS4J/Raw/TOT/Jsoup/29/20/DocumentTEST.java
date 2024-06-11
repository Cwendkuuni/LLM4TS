import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://www.example.com");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        Assert.assertEquals("html", doc.nodeName());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Assert.assertNull(doc.head());

        doc.appendElement("head");
        Assert.assertNotNull(doc.head());
        Assert.assertEquals("head", doc.head().nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Assert.assertNull(doc.body());

        doc.appendElement("body");
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testTitleWithEmptyDocument() {
        Document doc = new Document("http://www.example.com");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        Document doc = new Document("http://www.example.com");
        doc.head().appendElement("title").text("Example Title");
        Assert.assertEquals("Example Title", doc.title());
    }

    @Test
    public void testTitleWithNoTitleElement() {
        Document doc = new Document("http://www.example.com");
        doc.head().appendElement("meta").attr("charset", "UTF-8");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        Document doc = new Document("http://www.example.com");
        doc.head().appendElement("title").text("Example Title");
        
        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
    }

    @Test
    public void testSetTitleWithNoTitleElement() {
        Document doc = new Document("http://www.example.com");
        doc.head().appendElement("meta").attr("charset", "UTF-8");
        
        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
        Assert.assertTrue(doc.head().getElementsByTag("title").size() > 0);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://www.example.com");
        doc.body().appendElement("p").text("Example Text");
        
        doc.text("New Text");
        Assert.assertEquals("New Text", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://www.example.com");
        Document clone = doc.clone();

        Assert.assertEquals(doc.outerHtml(), clone.outerHtml());
        Assert.assertNotSame(doc, clone);
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        Assert.assertNotNull(outputSettings);
        Assert.assertTrue(outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("http://www.example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();
        
        Assert.assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        doc.quirksMode(Document.QuirksMode.quirks);
        Assert.assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://www.example.com");
        doc.head().appendElement("meta").attr("charset", "UTF-8");

        doc.createElement("div");
        Assert.assertTrue(doc.head().getElementsByTag("div").size() > 0);
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        doc.head().appendElement("meta").attr("charset", "UTF-8");

        doc.normalise();
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertFalse(doc.head().getElementsByTag("meta").isEmpty());
    }

}