import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://www.example.com");
        assertEquals("", document.title());

        document.title("Example Page");
        assertEquals("Example Page", document.title());

        document.head().appendElement("title").text("Custom Title");
        assertEquals("Custom Title", document.title());
    }

    @Test
    public void testcreateElement() {
        Document document = new Document("http://www.example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://www.example.com");
        document.createElement("div").text("Hello").appendChild(document.createElement("span").text("World"));

        assertEquals("<html>\n <head></head>\n <body>\n  <div>\n   Hello<span>World</span></div></body></html>", document.outerHtml());

        document.normalise();
        assertEquals("<html>\n <head></head>\n <body>\n  <div></div>\n  Hello\n  <span>World</span></body></html>", document.outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://www.example.com");
        document.createElement("div").text("Hello").appendChild(document.createElement("span").text("World"));

        assertEquals("<html>\n <head></head>\n <body>\n  <div>\n   Hello<span>World</span></div></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://www.example.com");
        document.text("Hello World");

        assertEquals("Hello World", document.body().text());
    }
}