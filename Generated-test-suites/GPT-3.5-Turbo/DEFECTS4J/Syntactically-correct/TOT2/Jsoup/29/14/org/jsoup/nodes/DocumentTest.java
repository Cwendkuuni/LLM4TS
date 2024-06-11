import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element elem = doc.createElement("a");
        assertNotNull(elem);
        assertEquals("a", elem.tagName());
        assertEquals("http://example.com", elem.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        head.appendText("Head text");
        Element body = doc.body();
        body.appendText("Body text");

        doc.normalise();
        assertEquals("Head text", head.text());
        assertEquals("Body text", body.text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();
        assertNotNull(clone);
        assertNotSame(doc, clone);
        assertEquals(doc.outputSettings(), clone.outputSettings());
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