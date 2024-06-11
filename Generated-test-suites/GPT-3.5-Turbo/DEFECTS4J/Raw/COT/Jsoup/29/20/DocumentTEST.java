import org.jsoup.parser.Tag;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        assertNull(document.head());
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        assertNull(document.body());
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleNoElement() {
        Document document = new Document("http://example.com");
        document.getElementsByTag("title").remove();
        assertEquals("", document.title());
        document.title("New Title");
        assertEquals("New Title", document.title());
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
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element div = body.appendElement("div");
        Element span = div.appendElement("span");
        div.text("   ");
        span.text("Hello World");

        document.normalise();

        Elements elements = document.getElementsContainingText("Hello World");
        assertEquals(0, elements.size());

        Element newBody = document.body();
        assertNotNull(newBody);
        assertEquals("Hello World", newBody.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("<title>Test Title</title>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.body().text("Hello World");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document document = new Document("http://example.com");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        document.title("Test Title");

        Document clone = document.clone();

        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.title(), clone.title());
        assertEquals(document.outerHtml(), clone.outerHtml());
        assertNotSame(document, clone);
        assertNotSame(document.head(), clone.head());
        assertNotSame(document.body(), clone.body());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        document.outputSettings(outputSettings);
        assertSame(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document("http://example.com");
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}