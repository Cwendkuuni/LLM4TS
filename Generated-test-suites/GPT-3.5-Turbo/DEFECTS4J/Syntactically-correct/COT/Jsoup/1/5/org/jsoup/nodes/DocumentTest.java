
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
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
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = document.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = document.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNull(head);
        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNull(body);
        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);
        document.appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title.trim());
    }

    @Test
    public void testTitleSet() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Element title = document.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.appendElement("div").text("Sample Text");
        document.normalise();
        Element body = document.body();
        assertNotNull(body);
        assertEquals("Sample Text", body.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.appendElement("body");
        document.text("Sample Text");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("Sample Text", body.text().trim());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("", outerHtml);
    }
}
