import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals(Tag.valueOf("#root"), document.tagName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals(Tag.valueOf("html"), document.tagName());
        Assert.assertEquals(1, document.children().size());
        Assert.assertEquals(3, document.children().get(0).children().size());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Assert.assertNull(document.head());

        document.appendElement("head");
        Assert.assertNotNull(document.head());
        Assert.assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Assert.assertNull(document.body());

        document.appendElement("body");
        Assert.assertNotNull(document.body());
        Assert.assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("", document.title());

        document.head().appendElement("title").text("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());

        document.head().removeClass("title");
        document.title("Updated Title");
        Assert.assertEquals("Updated Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("a", document.createElement("a").tagName());
        Assert.assertEquals("div", document.createElement("div").tagName());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.appendElement("head");
        document.head().appendElement("title").text("Test Title");
        document.appendElement("body");
        document.body().appendElement("div").text("Test Content");

        document.normalise();

        Assert.assertTrue(document.children().get(0) instanceof Element);
        Assert.assertEquals("html", document.children().get(0).tagName());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals(1, document.body().children().size());
        Assert.assertEquals("div", document.body().children().get(0).tagName());
        Assert.assertEquals("Test Content", document.body().children().get(0).text());
        Assert.assertEquals("", document.head().text());
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("", document.outerHtml());

        document.appendElement("head");
        document.head().appendElement("title").text("Test Title");
        Assert.assertEquals("", document.outerHtml());
        Assert.assertEquals("<title>Test Title</title>", document.head().outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        Assert.assertNull(document.text());

        document.body().text("Test Content");
        Assert.assertEquals("Test Content", document.text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testEscapeMode() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testCharset() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.charset("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testPrettyPrint() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.indentAmount(2);
        Assert.assertEquals(2, outputSettings.indentAmount());
    }

}