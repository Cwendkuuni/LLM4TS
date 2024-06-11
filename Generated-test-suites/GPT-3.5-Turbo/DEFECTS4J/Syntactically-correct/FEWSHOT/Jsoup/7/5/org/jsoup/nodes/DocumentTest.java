import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        
        assertNull(doc.head());
        
        doc.appendElement("head");
        assertEquals("head", doc.head().nodeName());
        
        doc.appendElement("title");
        assertEquals("title", doc.head().child(0).nodeName());
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        
        assertNull(doc.body());
        
        doc.appendElement("body");
        assertEquals("body", doc.body().nodeName());
        
        doc.body().appendElement("div");
        assertEquals("div", doc.body().child(0).nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        
        assertEquals("", doc.title());
        
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        
        // Test with no existing head or body
        doc.normalise();
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        
        // Test with existing head and body
        doc.head().appendElement("title");
        doc.body().appendElement("div");
        
        doc.normalise();
        assertEquals("html", doc.nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals("title", doc.head().child(0).nodeName());
        assertEquals("div", doc.body().child(0).nodeName());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.appendElement("body");
        
        assertEquals("", doc.text());
        
        doc.text("Test text");
        assertEquals("Test text", doc.text());
        
        doc.text("New text");
        assertEquals("New text", doc.text());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://www.example.com");
        
        assertEquals(Document.OutputSettings.EscapeMode.base, doc.outputSettings().escapeMode());
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());
        assertEquals(true, doc.outputSettings().prettyPrint());
        assertEquals(1, doc.outputSettings().indentAmount());
        
        doc.outputSettings().escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, doc.outputSettings().escapeMode());
        
        doc.outputSettings().charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
        
        doc.outputSettings().prettyPrint(false);
        assertEquals(false, doc.outputSettings().prettyPrint());
        
        doc.outputSettings().indentAmount(2);
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}