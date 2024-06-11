import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("http://www.example.com");
        assertNotNull("Document object is null", document);
        assertEquals("Base URI is not set correctly", "http://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull("Document object is null", document);
        assertEquals("Base URI is not set correctly", "http://www.example.com", document.baseUri());
        assertNotNull("Head element is null", document.head());
        assertNotNull("Body element is null", document.body());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull("Head element is null", document.head());
        assertEquals("Head element tag name is not 'head'", "head", document.head().tagName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull("Body element is null", document.body());
        assertEquals("Body element tag name is not 'body'", "body", document.body().tagName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://www.example.com");
        String title = "Example Title";
        document.title(title);
        assertEquals("Title is not set correctly", title, document.title());
    }

    @Test
    public void testElementCreation() {
        Document document = Document.createShell("http://www.example.com");
        String tagName = "div";
        Element element = document.createElement(tagName);
        assertNotNull("Element is null", element);
        assertEquals("Element tag name is not 'div'", tagName, element.tagName());
        assertEquals("Element base URI is not set correctly", "http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://www.example.com");
        String html = "<html><head><title>Example Title</title></head><body><p>Hello, world!</p></body></html>";
        document.html(html);
        document.normalise();
        assertNotNull("HTML element is null", document.getElementsByTag("html").first());
        assertNotNull("Head element is null", document.head());
        assertNotNull("Body element is null", document.body());
        assertNotNull("Title element is null", document.title());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://www.example.com");
        String html = "<p>Hello, world!</p>";
        document.body().html(html);
        assertEquals("Outer HTML is not the same as HTML", html, document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://www.example.com");
        String text = "Hello, world!";
        document.text(text);
        assertEquals("Text is not set correctly", text, document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals("Node name is not '#document'", "#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://www.example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull("OutputSettings object is null", outputSettings);
        assertEquals("Escape mode is not 'base'", Document.Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("Charset is not 'UTF-8'", "UTF-8", outputSettings.charset().name());
        assertTrue("Pretty print is not enabled by default", outputSettings.prettyPrint());
        assertEquals("Indent amount is not 1 by default", 1, outputSettings.indentAmount());
    }
}