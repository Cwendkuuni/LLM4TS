import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("https://www.example.com");
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>", shellDoc.outerHtml());
    }
    
    @Test
    public void testHead() {
        document.head().appendElement("title").text("Test Document");
        Assert.assertEquals("<head>\n" +
                " <title>Test Document</title>\n" +
                "</head>", document.head().outerHtml());
    }
    
    @Test
    public void testBody() {
        document.body().appendElement("div").addClass("container");
        Assert.assertEquals("<body>\n" +
                " <div class=\"container\"></div>\n" +
                "</body>", document.body().outerHtml());
    }
    
    @Test
    public void testTitle() {
        document.title("Test Document");
        Assert.assertEquals("Test Document", document.title());
    }
    
    @Test
    public void testTitleNoExistingTitleElement() {
        document.head().appendElement("meta").attr("charset", "UTF-8");
        document.title("Test Document");
        Assert.assertEquals("<head>\n" +
                " <meta charset=\"UTF-8\">\n" +
                " <title>Test Document</title>\n" +
                "</head>", document.head().outerHtml());
    }
    
    @Test
    public void testTitleExistingTitleElement() {
        document.head().appendElement("title").text("Old Title");
        document.title("Test Document");
        Assert.assertEquals("<head>\n" +
                " <title>Test Document</title>\n" +
                "</head>", document.head().outerHtml());
    }
    
    @Test
    public void testCreateElement() {
        document.body().appendChild(document.createElement("div").addClass("container"));
        Assert.assertEquals("<body>\n" +
                " <div class=\"container\"></div>\n" +
                "</body>", document.body().outerHtml());
    }
    
    @Test
    public void testNormalise() {
        document.head().appendElement("meta").attr("charset", "UTF-8");
        document.normalise();
        Assert.assertEquals("<html>\n" +
                " <head>\n" +
                "  <meta charset=\"UTF-8\"></head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());
    }
    
    @Test
    public void testNormaliseTextNodes() {
        document.head().appendTextNode("   \n\t");
        document.normalise();
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());
    }
    
    @Test
    public void testNormaliseStructure() {
        document.head().appendElement("meta").attr("charset", "UTF-8");
        document.head().appendElement("link").attr("rel", "stylesheet").attr("href", "style.css");
        document.head().appendElement("link").attr("rel", "icon").attr("href", "favicon.ico");
        document.normalise();
        Assert.assertEquals("<html>\n" +
                " <head>\n" +
                "  <meta charset=\"UTF-8\"></head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());
    }
    
    @Test
    public void testFindFirstElementByTagName() {
        document.body().appendElement("div").appendElement("p").text("Hello World");
        Assert.assertEquals("p", document.findFirstElementByTagName("p", document).nodeName());
    }
    
    @Test
    public void testOuterHtml() {
        document.body().appendElement("div").addClass("container");
        Assert.assertEquals("<div class=\"container\"></div>", document.outerHtml());
    }
    
    @Test
    public void testText() {
        document.text("Hello World");
        Assert.assertEquals("Hello World", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        Assert.assertEquals("#document", document.nodeName());
    }
    
    @Test
    public void testClone() {
        Document cloneDoc = document.clone();
        Assert.assertEquals(document.outerHtml(), cloneDoc.outerHtml());
    }
    
    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Entities.escapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.escapeMode.extended);
        Assert.assertEquals(Entities.escapeMode.extended, outputSettings.escapeMode());
    }
    
    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        Assert.assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }
    
    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(true, outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        Assert.assertEquals(false, outputSettings.prettyPrint());
    }
    
    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(2);
        Assert.assertEquals(2, outputSettings.indentAmount());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings newOutputSettings = new Document.OutputSettings().prettyPrint(false).indentAmount(2);
        document.outputSettings(newOutputSettings);
        Assert.assertEquals(newOutputSettings.prettyPrint(), document.outputSettings().prettyPrint());
        Assert.assertEquals(newOutputSettings.indentAmount(), document.outputSettings().indentAmount());
    }
    
    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        Assert.assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        document.quirksMode(Document.QuirksMode.limitedQuirks);
        Assert.assertEquals(Document.QuirksMode.limitedQuirks, document.quirksMode());
    }
}