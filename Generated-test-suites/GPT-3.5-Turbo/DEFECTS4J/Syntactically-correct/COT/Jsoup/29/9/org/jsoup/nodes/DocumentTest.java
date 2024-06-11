import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.head().parent().nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().nodeName());
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().nodeName());
        assertEquals(body, doc.body());
    }

    @Test 
    public void testTitle() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title").text("Example Domain");
        assertEquals("Example Domain", doc.title());
        title.text("New Title");
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        assertEquals(doc, doc.normalise());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(html.appendElement("head"));
        assertNotNull(html.appendElement("body"));
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.body().append("<p>Hello World</p>");
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Hello World");
        assertEquals("Hello World", doc.text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("http://example.com");
        doc.outputSettings(new Document.OutputSettings().charset("UTF-8").prettyPrint(false));
        Document clone = doc.clone();
        assertEquals(doc.outputSettings().charset(), clone.outputSettings().charset());
        assertEquals(doc.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
    }

}