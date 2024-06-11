import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
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

        assertNotNull("Shell should not be null", shell);
        assertNotNull("Head element should not be null", shell.head());
        assertNotNull("Body element should not be null", shell.body());
        assertEquals("Shell should have html element", "html", shell.select("html").first().tagName());
        assertEquals("Shell should have head element", "head", shell.select("head").first().tagName());
        assertEquals("Shell should have body element", "body", shell.select("body").first().tagName());
    }

    @Test
    public void testHead() {
        assertNull("Head should be null", document.head());

        Elements head = document.appendElement("head");

        assertNotNull("Head should not be null after appending element", document.head());
        assertSame("Head should be the same as the appended element", head, document.head());
    }

    @Test
    public void testBody() {
        assertNull("Body should be null", document.body());

        Elements body = document.appendElement("body");

        assertNotNull("Body should not be null after appending element", document.body());
        assertSame("Body should be the same as the appended element", body, document.body());
    }

    @Test
    public void testTitle() {
        String title = document.title();

        assertEquals("Title should be empty", "", title);

        document.title("Test Title");

        title = document.title();

        assertEquals("Title should be set to Test Title", "Test Title", title);

        document.appendElement("title").text("New Title");
        title = document.title();

        assertEquals("Title should be updated to New Title", "New Title", title);
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Element element = document.createElement(tagName);

        assertNotNull("Element should not be null", element);
        assertEquals("Element tag name should be div", tagName, element.tagName());
        assertEquals("Element base URI should be http://example.com", "http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document html = Document.createShell("http://example.com");
        html.normalise();

        assertEquals("Outer HTML should be equal after normalisation", "<html><head></head><body></body></html>", html.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Test Text";

        document.text(text);

        assertEquals("Text should be set to Test Text", text, document.text());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();

        assertNotNull("Clone should not be null", clone);
        assertNotSame("Clone should not be the same as the original document", document, clone);
        assertEquals("Clone should have the same outer HTML as the original document", document.outerHtml(), clone.outerHtml());
        assertNotSame("Clone outputSettings should be different object to the original document", document.outputSettings(), clone.outputSettings());
        assertEquals("Clone outputSettings should have the same escapeMode as the original document", document.outputSettings().escapeMode(), clone.outputSettings().escapeMode());
        assertEquals("Clone outputSettings should have the same charset as the original document", document.outputSettings().charset(), clone.outputSettings().charset());
        assertEquals("Clone outputSettings should have the same prettyPrint value as the original document", document.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
        assertEquals("Clone outputSettings should have the same indentAmount as the original document", document.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull("Output settings should not be null", outputSettings);
        assertSame("Output settings should be the same object as the one set in the document", outputSettings, document.outputSettings());

        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);

        assertNotSame("Output settings should be a different object after setting a new one", outputSettings, document.outputSettings());
        assertSame("Output settings should be the same object as the newly set one", newOutputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertNotNull("Quirks mode should not be null", quirksMode);
        assertEquals("Default quirks mode should be noQuirks", Document.QuirksMode.noQuirks, quirksMode);

        Document.QuirksMode newQuirksMode = Document.QuirksMode.quirks;
        document.quirksMode(newQuirksMode);

        assertNotSame("Quirks mode should not be the same object after setting a new one", quirksMode, document.quirksMode());
        assertSame("Quirks mode should be the same object as the newly set one", newQuirksMode, document.quirksMode());
    }
}