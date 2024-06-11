import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
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

        Assert.assertNotNull("Shell document should not be null", shell);
        Assert.assertEquals("Shell document should have 'html' as root element",
                Tag.html, shell.tagName());
        Assert.assertNotNull("Shell document should have 'head' element", shell.head());
        Assert.assertNotNull("Shell document should have 'body' element", shell.body());
    }

    @Test
    public void testHead() {
        Assert.assertNotNull("Head element should not be null", document.head());
    }

    @Test
    public void testBody() {
        Assert.assertNotNull("Body element should not be null", document.body());
    }

    @Test
    public void testTitleWithTitleElement() {
        document.head().appendElement("title").text("Test Title");

        Assert.assertEquals("Title should be 'Test Title'", "Test Title", document.title());
    }

    @Test
    public void testTitleWithoutTitleElement() {
        Assert.assertEquals("Title should be an empty string", "", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleWithNullTitle() {
        document.title(null);
    }

    @Test
    public void testTitleAddToHead() {
        document.title("Test Title");

        Assert.assertNotNull("Title element should not be null", document.head().getElementsByTag("title").first());
    }

    @Test
    public void testTitleUpdateExistingTitle() {
        document.head().appendElement("title").text("Old Title");
        document.title("New Title");

        String updatedTitle = document.head().getElementsByTag("title").first().text();

        Assert.assertEquals("Title should be 'New Title'", "New Title", updatedTitle);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateElementWithNullTagName() {
        document.createElement(null);
    }

    @Test
    public void testCreateElementWithValidTagName() {
        Document newElementDoc = document.createElement("div").ownerDocument();

        Assert.assertEquals("Owner document should be the same as the current document", document, newElementDoc);
        Assert.assertEquals("New element should have 'div' as tag name", "div", newElementDoc.tagName());
    }

    @Test
    public void testNormaliseWithEmptyDocument() {
        Document emptyDoc = new Document("http://example.com");

        emptyDoc.normalise();

        Assert.assertNotNull("HTML element should not be null after normalisation", emptyDoc.getElementsByTag("html").first());
        Assert.assertNotNull("Head element should not be null after normalisation", emptyDoc.head());
        Assert.assertNotNull("Body element should not be null after normalisation", emptyDoc.body());
    }

    @Test
    public void testNormaliseWithMissingHead() {
        document.head().remove();

        document.normalise();

        Assert.assertNotNull("Head element should be created after normalisation", document.head());
    }

    @Test
    public void testNormaliseWithMissingBody() {
        document.body().remove();

        document.normalise();

        Assert.assertNotNull("Body element should be created after normalisation", document.body());
    }

    @Test
    public void testNormaliseWithTextNodes() {
        document.prependText("Text Node 1");
        document.appendText("Text Node 2");

        document.normalise();

        Assert.assertEquals("Normalized document should have two text nodes within the body", 2, document.body().textNodes().size());
    }

    @Test
    public void testOuterHtml() {
        Assert.assertEquals("Outer HTML should be the same as HTML", document.html(), document.outerHtml());
    }

    @Test
    public void testSetText() {
        String text = "Test Text";

        document.text(text);

        Assert.assertEquals("Text within the body should be " + text, text, document.body().text());
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("Node name should be '#document'", "#document", document.nodeName());
    }
}