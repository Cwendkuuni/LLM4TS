import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull("HTML element should not be null", html);
        assertNotNull("Head element should not be null", head);
        assertNotNull("Body element should not be null", body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = "Test Title";
        doc.title(title);

        assertEquals("Title should match", title, doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.appendElement("div").text("This is a test");

        doc.normalise();

        assertEquals("Body should contain text", "This is a test", doc.body().text());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        String text = "Test text";
        doc.text(text);

        assertEquals("Body text should match", text, doc.body().text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        Document clone = doc.clone();

        assertFalse("Clone should not be the same object", doc == clone);
        assertEquals("Clone should have same output settings", doc.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        doc.outputSettings(outputSettings);

        assertEquals("Output settings should match", outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;

        doc.quirksMode(quirksMode);

        assertEquals("Quirks mode should match", quirksMode, doc.quirksMode());
    }
}