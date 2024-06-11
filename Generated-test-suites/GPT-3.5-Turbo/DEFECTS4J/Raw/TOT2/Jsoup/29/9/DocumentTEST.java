import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = doc.head();
        assertNotNull(head);
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        // Add assertions for normalized structure
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test text");
        assertEquals("Test text", doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertNotSame(doc, clone);
        assertEquals(doc.outputSettings().charset(), clone.outputSettings().charset());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        doc.outputSettings(outputSettings);
        assertFalse(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
    }

}