import org.junit.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.nodeName());
        Assert.assertEquals("head", document.head().nodeName());
        Assert.assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("https://www.example.com");
        Assert.assertNotNull(document.head());
        Assert.assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("https://www.example.com");
        Assert.assertNotNull(document.body());
        Assert.assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("https://www.example.com");
        Assert.assertEquals("", document.title());
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        Document document = Document.createShell("https://www.example.com");
        document.head().appendElement("title").text("Existing Title");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithNullTitle() {
        Document document = Document.createShell("https://www.example.com");
        document.title(null);
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testTitleWithNullTitleElement() {
        Document document = Document.createShell("https://www.example.com");
        document.head().getElementsByTag("title").remove();
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("https://www.example.com");
        Element element = document.createElement("a");
        Assert.assertNotNull(element);
        Assert.assertEquals("a", element.nodeName());
        Assert.assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://www.example.com");
        Element element = document.normalise().head();
        Assert.assertEquals("head", element.nodeName());
        Assert.assertEquals("html", element.parent().nodeName());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("https://www.example.com");
        document.text("Test Text");
        Assert.assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("https://www.example.com");
        String outerHtml = document.outerHtml();
        Assert.assertNotNull(outerHtml);
        Assert.assertEquals("<html><head></head><body></body></html>", outerHtml);
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("https://www.example.com");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document document = Document.createShell("https://www.example.com");
        Document clone = document.clone();
        Assert.assertNotNull(clone);
        Assert.assertNotSame(document, clone);
        Assert.assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("https://www.example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);
        Assert.assertNotNull(outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertEquals(true, outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        Document.OutputSettings newSettings = new Document.OutputSettings();
        newSettings.escapeMode(OutputSettings.EscapeMode.extended);
        newSettings.charset("ISO-8859-1");
        newSettings.prettyPrint(false);
        newSettings.indentAmount(2);

        document.outputSettings(newSettings);

        Assert.assertEquals(newSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document document = Document.createShell("https://www.example.com");
        Assert.assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());

        document.quirksMode(Document.QuirksMode.quirks);
        Assert.assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}