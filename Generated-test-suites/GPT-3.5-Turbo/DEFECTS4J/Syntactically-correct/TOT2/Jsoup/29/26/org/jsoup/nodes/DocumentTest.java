import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.baseUri());
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        // Add specific test cases for normalise method
    }

    @Test
    public void testOuterHtml() {
        assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test text");
        assertEquals("Test text", document.body().text());
    }

    @Test
    public void testClone() {
        Document clonedDocument = document.clone();
        assertNotNull(clonedDocument);
        assertEquals(document.outputSettings().charset(), clonedDocument.outputSettings().charset());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset("UTF-8");
        document.outputSettings(outputSettings);

        assertNotNull(document.outputSettings());
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testQuirksMode() {
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}