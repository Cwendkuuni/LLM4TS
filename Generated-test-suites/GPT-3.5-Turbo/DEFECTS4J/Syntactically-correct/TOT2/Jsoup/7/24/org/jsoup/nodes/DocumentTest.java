import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element elem = doc.createElement("a");
        assertNotNull(elem);
        assertEquals("a", elem.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("div").text("First");
        doc.appendElement("div").text("Second");
        doc.normalise();
        assertEquals("First Second", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        assertEquals(Document.OutputSettings.EscapeMode.base, doc.outputSettings().escapeMode());
        assertEquals(Charset.forName("UTF-8"), doc.outputSettings().charset());
        assertTrue(doc.outputSettings().prettyPrint());
        assertEquals(1, doc.outputSettings().indentAmount());
    }

    // Add more tests for remaining methods

}