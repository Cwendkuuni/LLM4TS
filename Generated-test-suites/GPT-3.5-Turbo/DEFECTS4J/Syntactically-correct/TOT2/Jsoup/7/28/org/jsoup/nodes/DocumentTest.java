import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
    
    @Test
    public void testDocumentCreation() {
        Document doc = new Document("example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("example.com", doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("example.com");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("example.com", doc.baseUri());
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("example.com");
        Element div = doc.createElement("div");
        div.text("Hello there");
        doc.body().appendChild(div);
        doc.normalise();
        assertEquals("Hello there", doc.body().text());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}