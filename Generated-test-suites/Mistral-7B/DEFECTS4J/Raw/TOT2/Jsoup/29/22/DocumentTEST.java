import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.tagName());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        Assert.assertNotNull(document);
        Assert.assertTrue(document.head() != null);
        Assert.assertTrue(document.body() != null);
    }

    @Test
    public void testGetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        document.title("New Title");
        Assert.assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        Element title = document.appendElement("title");
        title.text("Test Title");
        Element body = document.body();
        body.text("Test Body");
        document.appendElement("body");

        document.normalise();

        Assert.assertEquals(1, document.getElementsByTag("html").size());
        Assert.assertEquals(1, document.getElementsByTag("head").size());
        Assert.assertEquals(1, document.getElementsByTag("body").size());
        Assert.assertEquals("Test Title", document.title());
        Assert.assertEquals("Test Body", document.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameWithNullNode() {
        Document document = Document.createShell("http://example.com");
        findFirstElementByTagName(null, "html");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameWithInvalidTagName() {
        Document document = Document.createShell("http://example.com");
        findFirstElementByTagName(document, "zoo");
    }

    private Element findFirstElementByTagName(Node node, String tag) {
        if (node.nodeName().equals(tag))
            return (Element) node;
        else {
            for (Node child: node.childNodes) {
                Element found = findFirstElementByTagName(child, tag);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        document.body().text("Test Body");
        Assert.assertEquals("<html><head></head><body>Test Body</body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        document.body().text("Test Body");
        Assert.assertEquals("Test Body", document.text());
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCloneDocument() {
        Document document = Document.createShell("http://example.com");
        Document clonedDocument = document.clone();
        Assert.assertEquals(document.title(), clonedDocument.title());
        Assert.assertEquals(document.head(), clonedDocument.head());
        Assert.assertEquals(document.body(), clonedDocument.body());
        Assert.assertNotSame(document, clonedDocument);
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://example.com");
        OutputSettings settings = document.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        Assert.assertEquals("UTF-8", settings.charset().name());
        Assert.assertTrue(settings.prettyPrint());

        OutputSettings newSettings = settings.escapeMode(Entities.EscapeMode.extended)
                .charset("ISO-8859-1")
                .prettyPrint(false);
        document.outputSettings(newSettings);
        Assert.assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset().name());
        Assert.assertFalse(document.outputSettings().prettyPrint());
    }
}