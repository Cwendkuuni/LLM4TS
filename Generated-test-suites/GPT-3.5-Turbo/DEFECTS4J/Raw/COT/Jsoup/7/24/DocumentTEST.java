import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentTest {
    
    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        Assert.assertEquals("head", head.nodeName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        Assert.assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        String title = doc.title();
        Assert.assertEquals("", title);
        doc.title("Test Title");
        title = doc.title();
        Assert.assertEquals("Test Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element element = doc.createElement("a");
        Assert.assertEquals("a", element.nodeName());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        Document normalizedDoc = doc.normalise();
        Assert.assertEquals(doc, normalizedDoc);
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://www.example.com");
        String outerHtml = doc.outerHtml();
        Assert.assertEquals("", outerHtml);
    }
    
    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Test Text");
        String text = doc.text();
        Assert.assertEquals("Test Text", text);
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        Assert.assertNotNull(outputSettings);
        outputSettings.escapeMode(Document.Entities.EscapeMode.extended);
        Assert.assertEquals(Document.Entities.EscapeMode.extended, outputSettings.escapeMode());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testEmptyTitle() {
        Document doc = Document.createShell("https://www.example.com");
        String emptyTitle = doc.title();
        Assert.assertEquals("", emptyTitle);
    }
    
    @Test
    public void testEmptyElementCreation() {
        Document doc = Document.createShell("https://www.example.com");
        Element emptyElement = doc.createElement("");
        Assert.assertNull(emptyElement);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        Document doc = Document.createShell("https://www.example.com");
        doc.title(null);
    }
    
    @Test
    public void testEmptyOutputSettings() {
        Document doc = new Document("https://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }
    
    @Test
    public void testDisablePrettyPrint() {
        Document doc = new Document("https://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndentAmount() {
        Document doc = new Document("https://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        outputSettings.indentAmount(-1);
    }

}