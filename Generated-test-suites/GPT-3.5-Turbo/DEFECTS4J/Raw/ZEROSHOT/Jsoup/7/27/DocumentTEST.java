import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        Assert.assertNotNull(shell);
        Assert.assertEquals("http://example.com", shell.baseUri());
        Element html = shell.getElementsByTag("html").first();
        Assert.assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        Assert.assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        Assert.assertNotNull(body);
    }

    @Test
    public void testHead() {
        Assert.assertNull(document.head());
        Element head = document.appendElement("head");
        Assert.assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        Assert.assertNull(document.body());
        Element body = document.appendElement("body");
        Assert.assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        Assert.assertEquals("", document.title());
        document.head().appendElement("title").text("Test Title");
        Assert.assertEquals("Test Title", document.title());
        document.title("New Title");
        Assert.assertEquals("New Title", document.title());
    }

    @Test
    public void testNormalise() {
        document.body().text("Test Text");
        document.normalise();
        Assert.assertEquals("Test Text", document.body().text());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testFirstChildByTagName() {
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Assert.assertEquals(html, document.firstChildByTagName("html", document));
        Assert.assertEquals(head, document.firstChildByTagName("head", document));
        Assert.assertEquals(body, document.firstChildByTagName("body", document));
        Assert.assertNull(document.firstChildByTagName("div", document));
    }

    @Test
    public void testOuterHtml() {
        Assert.assertEquals("", document.outerHtml());
        document.body().text("Test Text");
        Assert.assertEquals("Test Text", document.outerHtml());
    }

    @Test
    public void testText() {
        Assert.assertEquals("", document.text());
        document.body().text("Test Text");
        Assert.assertEquals("Test Text", document.text());
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);
        Assert.assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        Assert.assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());

        outputSettings.charset("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", outputSettings.charset().name());

        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        Assert.assertEquals(2, outputSettings.indentAmount());
    }
}