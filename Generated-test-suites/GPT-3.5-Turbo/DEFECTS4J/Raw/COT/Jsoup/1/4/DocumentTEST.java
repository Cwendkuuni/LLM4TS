
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("html", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertNotNull(title);
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        String html = document.outerHtml();
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        String text = "This is some text";
        Element body = document.body();
        body.text(text);
        assertEquals(text, body.text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }
}
