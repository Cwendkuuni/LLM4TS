
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;
    
    @Before
    public void setup() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        Assert.assertNotNull(shell);
        Assert.assertEquals("html", shell.nodeName());
        Assert.assertEquals("http://example.com", shell.baseUri());
        Assert.assertNotNull(shell.head());
        Assert.assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        Assert.assertNull(head);
        
        Element headElement = document.appendElement("head");
        head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals(headElement, head);
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        Assert.assertNull(body);
        
        Element bodyElement = document.appendElement("body");
        body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals(bodyElement, body);
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        Assert.assertEquals("", title);
        
        document.appendElement("title").text("Example Title");
        title = document.title();
        Assert.assertEquals("Example Title", title);
    }
    
    @Test
    public void testSetTitle() {
        document.title("New Title");
        String title = document.title();
        Assert.assertEquals("New Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document htmlDocument = Document.createShell("http://example.com");
        Element bodyElement = htmlDocument.body();
        bodyElement.appendText("Hello");
        bodyElement.appendElement("h1").text("Title");
        
        Document normalisedDocument = htmlDocument.normalise();
        Element normalisedBody = normalisedDocument.body();
        
        Assert.assertEquals("Hello Title", normalisedBody.text());
        Assert.assertEquals(0, bodyElement.childNodes().size());
    }
    
    @Test
    public void testOuterHtml() {
        document.appendElement("div").text("Test");
        String outerHtml = document.outerHtml();
        Assert.assertEquals("<div>Test</div>", outerHtml);
    }
    
    @Test
    public void testText() {
        document.body().appendText("Test");
        String text = document.body().text();
        Assert.assertEquals("Test", text);
    }
    
    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
    
    @Test
    public void testClone() {
        Document clone = document.clone();
        Assert.assertNotEquals(document, clone);
        Assert.assertEquals(document.baseUri(), clone.baseUri());
        Assert.assertEquals(document.outputSettings(), clone.outputSettings());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);
        
        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);
        Assert.assertEquals(newOutputSettings, document.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        Assert.assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        
        document.quirksMode(Document.QuirksMode.quirks);
        quirksMode = document.quirksMode();
        Assert.assertEquals(Document.QuirksMode.quirks, quirksMode);
    }
}
