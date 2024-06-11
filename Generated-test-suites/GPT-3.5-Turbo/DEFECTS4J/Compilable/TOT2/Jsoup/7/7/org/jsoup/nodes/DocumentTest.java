package org.jsoup.nodes;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
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
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());
        assertEquals("UTF-8", outputSettings.charset().name());
    }
}