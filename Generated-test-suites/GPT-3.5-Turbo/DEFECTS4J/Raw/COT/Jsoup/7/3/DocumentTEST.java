import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNull(head);

        Element html = document.appendElement("html");
        html.appendElement("head");

        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNull(body);

        Element html = document.appendElement("html");
        html.appendElement("body");

        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);

        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        head.appendElement("title").text("Test Title");

        title = document.title();
        assertEquals("Test Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
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
        document.normalise();

        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        assertNotNull(document.head());
        assertNotNull(document.body());

        Element textElement = new Element(Tag.valueOf("p"), "");
        textElement.appendChild(new TextNode("Test Node"));
        document.appendChild(textElement);
        document.normalise();
        assertNull(document.getElementsByTag("p").first());
        assertNotNull(document.body().getElementsByTag("p").first());
        assertEquals("Test Node", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        head.appendElement("title").text("Test Title");
        Element body = html.appendElement("body");
        body.appendElement("p").text("Test Paragraph");

        String outerHtml = document.outerHtml();
        assertEquals("<head><title>Test Title</title></head><body><p>Test Paragraph</p></body>", outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test");
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        assertNotNull(document.outputSettings());
        assertEquals(Document.OutputSettings.class, document.outputSettings().getClass());
    }
}