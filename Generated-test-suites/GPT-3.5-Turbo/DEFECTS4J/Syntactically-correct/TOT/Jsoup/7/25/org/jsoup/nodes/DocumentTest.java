import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        Element html = shell.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        Assert.assertNotNull("HTML element should not be null", html);
        Assert.assertNotNull("Head element should not be null", head);
        Assert.assertNotNull("Body element should not be null", body);
    }

    @Test
    public void testHead() {
        Element head = document.head();

        Assert.assertNotNull("Head element should not be null", head);
        Assert.assertEquals("Head element tag name should be 'head'", "head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();

        Assert.assertNotNull("Body element should not be null", body);
        Assert.assertEquals("Body element tag name should be 'body'", "body", body.tagName());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Existing Title");
        String title = document.title();

        Assert.assertEquals("Title should be 'Existing Title'", "Existing Title", title);
    }

    @Test
    public void testTitleWithoutExistingTitleElement() {
        String title = document.title();

        Assert.assertEquals("Title should be an empty string", "", title);
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Existing Title");
        document.title("New Title");
        String title = document.title();

        Assert.assertEquals("Title should be 'New Title'", "New Title", title);
    }

    @Test
    public void testSetTitleWithoutExistingTitleElement() {
        document.title("New Title");
        String title = document.title();

        Assert.assertEquals("Title should be 'New Title'", "New Title", title);
    }

    @Test
    public void testNormalise() {
        Element body = document.body();
        body.appendText("Text outside body");

        document.normalise();

        Assert.assertFalse("Text outside body should be removed", body.text().contains("Text outside body"));
    }

    @Test
    public void testNormaliseTextNodes() {
        Element body = document.body();
        body.appendText("Text inside body");

        document.normalise();

        Assert.assertTrue("Text inside body should be normalized", body.text().equals("Text inside body"));
    }

    @Test
    public void testNormaliseNoHtmlElement() {
        document.normalise();

        Assert.assertTrue("Html element should be added when normalizing", document.getElementsByTag("html").size() == 1);
    }

    @Test
    public void testNormaliseNoHeadElement() {
        Element html = document.getElementsByTag("html").first();
        html.removeChild(html.getElementsByTag("head").first());

        document.normalise();

        Assert.assertTrue("Head element should be added when normalizing", document.head() != null);
    }

    @Test
    public void testNormaliseNoBodyElement() {
        Element html = document.getElementsByTag("html").first();
        html.removeChild(html.getElementsByTag("body").first());

        document.normalise();

        Assert.assertTrue("Body element should be added when normalizing", document.body() != null);
    }

    @Test
    public void testNormaliseTextNodesOrder() {
        Element body = document.body();
        body.appendText("First text node");
        body.appendText("Second text node");

        document.normalise();

        Assert.assertTrue("Text nodes should be in the correct order after normalization", body.text().equals("Second text node First text node"));
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        Assert.assertNotNull("Created element should not be null", element);
        Assert.assertEquals("Created element tag name should be 'div'", "div", element.tagName());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();

        Assert.assertEquals("Outer HTML should be the same as HTML", document.html(), outerHtml);
    }

    @Test
    public void testText() {
        String text = "This is the body text";
        document.text(text);

        Assert.assertEquals("Body text should be set correctly", text, document.body().text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();

        Assert.assertEquals("Node name should be '#document'", "#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Assert.assertNotNull("Output settings should not be null", outputSettings);
        Assert.assertEquals("Escape mode should be 'base'", Document.Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("Charset should be UTF-8", "UTF-8", outputSettings.charset().name());
        Assert.assertTrue("Pretty print should be enabled by default", outputSettings.prettyPrint());
        Assert.assertEquals("Indent amount should be 1", 1, outputSettings.indentAmount());
    }
}