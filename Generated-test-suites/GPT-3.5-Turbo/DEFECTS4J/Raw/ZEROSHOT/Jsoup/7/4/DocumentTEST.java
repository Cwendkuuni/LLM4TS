import org.junit.*;
import org.jsoup.nodes.*;

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
        Assert.assertEquals("html", shell.tagName());
        Assert.assertNotNull(shell.head());
        Assert.assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        Assert.assertNull(head);
        
        // Create head element and test again
        document.appendElement("head");
        head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        Assert.assertNull(body);
        
        // Create body element and test again
        document.appendElement("body");
        body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        Assert.assertEquals("", title);
        
        // Set title and test again
        document.title("Test Title");
        title = document.title();
        Assert.assertEquals("Test Title", title);
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
        // Normalise empty document
        document.normalise();
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals("html", document.tagName());
        
        // Normalise document with existing elements
        document.appendElement("title").text("Test Title");
        document.normalise();
        Assert.assertEquals("head", document.head().tagName());
        Assert.assertEquals("title", document.head().getElementsByTag("title").first().tagName());
        Assert.assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        Assert.assertEquals("", outerHtml);
        
        // Add child element and test again
        document.appendElement("div");
        outerHtml = document.outerHtml();
        Assert.assertEquals("<div></div>", outerHtml);
    }
    
    @Test
    public void testText() {
        String text = document.text();
        Assert.assertEquals("", text);
        
        // Set text and test again
        document.text("Test Text");
        text = document.text();
        Assert.assertEquals("Test Text", text);
    }
    
    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertEquals(true, outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }
}