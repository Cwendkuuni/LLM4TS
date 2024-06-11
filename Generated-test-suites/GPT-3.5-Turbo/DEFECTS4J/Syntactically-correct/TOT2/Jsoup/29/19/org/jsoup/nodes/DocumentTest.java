import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testText() {
        document.text("Test Body Text");
        assertEquals("Test Body Text", document.body().text());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}