import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testHead() {
        Element head = document.head();
        Assert.assertNotNull("Head element should not be null", head);
        Assert.assertEquals("Head tag name should be 'head'", "head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        Assert.assertNotNull("Body element should not be null", body);
        Assert.assertEquals("Body tag name should be 'body'", "body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        Assert.assertEquals("Title should be an empty string", "", title);

        document.title("Test Title");
        String newTitle = document.title();
        Assert.assertEquals("Title should be 'Test Title'", "Test Title", newTitle);
    }

    @Test
    public void testcreateElement() {
        Element element = document.createElement("div");
        Assert.assertNotNull("Created element should not be null", element);
        Assert.assertEquals("Element tag name should be 'div'", "div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        Assert.assertNotNull("Normalized document should not be null", normalizedDocument);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        Assert.assertNotNull("Outer HTML should not be null", outerHtml);
    }

    @Test
    public void testText() {
        String text = "Test content";
        document.text(text);
        Assert.assertEquals("Body text should be 'Test content'", text, document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull("Output settings should not be null", outputSettings);

        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);
        Assert.assertEquals("Output settings should be set to the new output settings", newOutputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        Assert.assertNotNull("Quirks mode should not be null", quirksMode);

        Document.QuirksMode newQuirksMode = Document.QuirksMode.noQuirks;
        document.quirksMode(newQuirksMode);
        Assert.assertEquals("Quirks mode should be set to the new quirks mode", newQuirksMode, document.quirksMode());
    }
}