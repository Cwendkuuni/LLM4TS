import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        Element p = doc.createElement("p");
        p.text("Test Paragraph");
        doc.appendChild(p);
        doc.normalise();
        assertEquals("Test Paragraph", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://www.example.com");
        Element p = doc.createElement("p");
        p.text("Test Paragraph");
        doc.appendChild(p);
        assertEquals("<p>Test Paragraph</p>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.text("Test Body Text");
        assertEquals("Test Body Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document().outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

}