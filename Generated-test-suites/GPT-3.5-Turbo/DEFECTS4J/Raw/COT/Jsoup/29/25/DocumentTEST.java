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

        Assert.assertNotNull(shell.head());
        Assert.assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();

        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();

        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        document.title("Test Title");

        Assert.assertEquals("Test Title", document.title());

        // Testing for null title element
        Document emptyDocument = Document.createShell("https://example.com");
        Assert.assertEquals("", emptyDocument.title());
    }

    @Test
    public void testTitleNull() {
        try {
            document.title(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            Assert.assertEquals("title must not be null", e.getMessage());
        }
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalized = document.normalise();

        Assert.assertNotNull(normalized.head());
        Assert.assertNotNull(normalized.body());

        // Testing for empty document
        Document emptyDocument = Document.createShell("https://example.com");
        Document normalizedEmpty = emptyDocument.normalise();

        Assert.assertNotNull(normalizedEmpty.head());
        Assert.assertNotNull(normalizedEmpty.body());
    }

    @Test
    public void testOuterHtml() {
        document.createElement("div").text("test");

        Assert.assertEquals("test", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("test");

        Assert.assertEquals("test", document.body().text());
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();

        Assert.assertEquals(document.outputSettings().charset().name(), clone.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        document.outputSettings(outputSettings);

        Assert.assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        document.quirksMode(Document.QuirksMode.quirks);

        Assert.assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}