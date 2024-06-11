import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
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
        String titleText = "Test Title";
        doc.title(titleText);
        assertEquals(titleText, doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.appendElement("div").text("Hello");
        doc.normalise();
        Element body = doc.body();
        assertEquals("Hello", body.text());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://example.com");
        Document clonedDoc = doc.clone();
        assertNotEquals(doc, clonedDoc);
        assertEquals(doc.outputSettings(), clonedDoc.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
        doc.quirksMode(quirksMode);
        assertEquals(quirksMode, doc.quirksMode());
    }
}