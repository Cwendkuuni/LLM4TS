
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {
    
    @Test
    public void testDocumentCreation() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertEquals("html", doc.nodeName());
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHeadAccessor() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().nodeName());
    }
    
    @Test
    public void testBodyAccessor() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals("", doc.title());
        
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());
        
        doc.head().appendElement("title").text("New Title");
        assertEquals("New Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.createElement("div"));
        assertEquals("div", doc.createElement("div").nodeName());
        assertEquals("https://www.example.com", doc.createElement("div").baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        doc.body().append("<p>Hello</p>");
        assertEquals("<html><head></head><body><p>Hello</p></body></html>", doc.html());
        
        doc.normalise();
        assertEquals("<html><head></head><body><p>Hello</p></body></html>", doc.html()); // No change expected
        
        doc.head().appendElement("title").text("Title");
        doc.normalise();
        assertEquals("<html><head><title>Title</title></head><body><p>Hello</p></body></html>", doc.html());
        
        doc.normalise();
        assertEquals("<html><head><title>Title</title></head><body><p>Hello</p></body></html>", doc.html()); // No change expected
        
        doc.createElement("meta").attr("charset", "UTF-8");
        doc.normalise();
        assertEquals("<html><head><title>Title</title><meta charset=\"UTF-8\"></head><body><p>Hello</p></body></html>", doc.html());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("https://www.example.com");
        doc.body().append("<p>Hello</p>");
        assertEquals("<p>Hello</p>", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        doc.body().append("<p>Hello</p><p>World</p>");
        assertEquals("HelloWorld", doc.text());
        
        doc.text("New Text");
        assertEquals("New Text", doc.text());
        assertEquals("<html><head></head><body>New Text</body></html>", doc.html());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://www.example.com");
        Document.OutputSettings outputSettings1 = new Document.OutputSettings();
        
        assertNotNull(doc.outputSettings());
        
        doc.outputSettings(outputSettings1);
        assertSame(outputSettings1, doc.outputSettings());
        
        Document.OutputSettings outputSettings2 = new Document.OutputSettings();
        outputSettings2.escapeMode(Document.OutputSettings.Entities.EscapeMode.extended);
        outputSettings2.charset("UTF-16");
        outputSettings2.prettyPrint(false);
        outputSettings2.indentAmount(2);
        
        doc.outputSettings(outputSettings2);
        assertSame(outputSettings2, doc.outputSettings());
        assertSame(Document.OutputSettings.Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
        assertEquals("UTF-16", doc.outputSettings().charset().name());
        assertFalse(doc.outputSettings().prettyPrint());
        assertEquals(2, doc.outputSettings().indentAmount());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
        
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}
