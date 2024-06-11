import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public void testConstructor() {
        Assert.assertEquals(Tag.valueOf("#root"), document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        
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
    public void testTitle_noTitleSet() {
        String title = document.title();

        Assert.assertEquals("", title);
    }

    @Test
    public void testTitle_withTitleSet() {
        document.title("Test Title");
        String title = document.title();

        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitle_updateExistingTitle() {
        document.title("First Title");
        document.title("Updated Title");
        String title = document.title();

        Assert.assertEquals("Updated Title", title);
    }

    @Test
    public void testTitle_addTitleToHead() {
        document.head().empty();
        document.title("Test Title");

        Assert.assertEquals("Test Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testTitle_removeExistingHeadTitle() {
        document.head().appendElement("title").text("Original Title");
        document.title("New Title");

        Assert.assertEquals("New Title", document.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testTitleNotBlank() {
        try {
            document.title(null);
            Assert.fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testTitleNotBlank2() {
        try {
            document.title("");
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();

        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        doc.normalise();

        String expectedHtml = "<html><head></head><body><pre> </pre></body></html>";
        String actualHtml = doc.html();

        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testOuterHtml() {
        String expectedHtml = "<html><head></head><body></body></html>";
        String actualHtml = document.outerHtml();

        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testTextNodeSetter() {
        document.body().appendText("Test Text");

        String expectedText = "Test Text";
        String actualText = document.body().text();

        Assert.assertEquals(expectedText, actualText);
    }

    @Test
    public void testNodeName() {
        String expectedName = "#document";
        String actualName = document.nodeName();

        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Assert.assertEquals(org.jsoup.nodes.Entities.EscapeMode.base, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Assert.assertTrue(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeModeSetter() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.escapeMode(org.jsoup.nodes.Entities.EscapeMode.extended);

        Assert.assertEquals(org.jsoup.nodes.Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharsetSetter() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset(Charset.forName("UTF-16"));

        Assert.assertEquals(Charset.forName("UTF-16"), outputSettings.charset());
    }
}