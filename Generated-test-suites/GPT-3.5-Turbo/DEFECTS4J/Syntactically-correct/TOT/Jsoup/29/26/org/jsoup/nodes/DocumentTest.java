import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        
        assertEquals("#root", document.nodeName());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();

        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();

        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");

        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);

        String newTitle = "Example Title";
        document.title(newTitle);
        assertEquals(newTitle, document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("html", document.child(0).nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");

        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.text("Example Text");

        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testClone() {
        Document document = Document.createShell("http://example.com");
        Document clone = document.clone();

        assertNotNull(clone);
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Document.OutputSettings.class, outputSettings.getClass());

        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());
        assertEquals(Document.OutputSettings.EscapeMode.extended, document.outputSettings().escapeMode());
    }

    @Test
    public void testQuirksMode() {
        Document document = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = document.quirksMode();

        assertNotNull(quirksMode);
        assertEquals(Document.QuirksMode.class, quirksMode.getClass());

        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}