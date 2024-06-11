import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.children().get(0).tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        doc.appendElement("head");
        doc.appendElement("body");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.children().get(0).tagName());
        assertEquals("body", doc.children().get(1).tagName());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        doc.outputSettings(outputSettings);
        assertFalse(doc.outputSettings().prettyPrint());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://www.example.com");
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }
}