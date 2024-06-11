import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertEquals(0, document.children().size());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.children().size());
        Element htmlElement = document.children().get(0);
        assertEquals("html", htmlElement.nodeName());
        assertEquals(2, htmlElement.children().size());
        Element headElement = htmlElement.children().get(0);
        Element bodyElement = htmlElement.children().get(1);
        assertEquals("head", headElement.nodeName());
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element headElement = document.head();
        assertNull(headElement);

        Element htmlElement = document.appendElement("html");
        htmlElement.appendElement("head");
        htmlElement.appendElement("body");

        headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        assertNull(bodyElement);

        Element htmlElement = document.appendElement("html");
        htmlElement.appendElement("head");
        htmlElement.appendElement("body");

        bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);

        document.appendElement("head").appendElement("title").text("Sample Title");

        title = document.title();
        assertEquals("Sample Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("New Title");
        assertEquals("<html><head><title>New Title</title></head><body></body></html>", document.html());

        document.appendElement("head").appendElement("title").text("Sample Title");
        document.title("Updated Title");
        assertEquals("<html><head><title>Updated Title</title></head><body></body></html>", document.html());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.appendElement("html").appendElement("body").appendElement("p").text("Text content");

        document.normalise();

        assertEquals("<html><head></head><body><p>Text content</p></body></html>", document.html());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.appendElement("html").appendElement("body").appendElement("p").text("Text content");

        assertEquals("<html><head></head><body><p>Text content</p></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.appendElement("html").appendElement("body").appendElement("p").text("Text content");

        document.text("New Text");

        Element bodyElement = document.body();
        assertEquals("New Text", bodyElement.text());
        assertEquals("<html><head></head><body>New Text</body></html>", document.html());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}