import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("<html><head></head><body></body></html>", shell.outerHtml());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.appendElement("html");
        document.appendElement("head");
        document.appendElement("body");

        document.normalise();
        assertEquals("<html><head></head><body></body></html>", document.outerHtml());

        document.head().appendElement("title").text("Test Title");
        document.normalise();
        assertEquals("<html><head><title>Test Title</title></head><body></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.appendElement("body");

        document.text("Test Text");
        Element body = document.body();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testOuterHtml() {
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        String outerHtml = document.outerHtml();
        assertEquals("<html><head></head><body></body></html>", outerHtml);
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals(document.outerHtml(), clone.outerHtml());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.prettyPrint(false).indentAmount(2);
        Document updatedDocument = document.outputSettings(outputSettings);

        Document.OutputSettings updatedOutputSettings = updatedDocument.outputSettings();
        assertNotNull(updatedOutputSettings);
        assertFalse(updatedOutputSettings.prettyPrint());
        assertEquals(2, updatedOutputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertNotNull(quirksMode);
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        document.quirksMode(Document.QuirksMode.quirks);
        Document updatedDocument = document.quirksMode(quirksMode);

        Document.QuirksMode updatedQuirksMode = updatedDocument.quirksMode();
        assertNotNull(updatedQuirksMode);
        assertEquals(quirksMode, updatedQuirksMode);
    }
}