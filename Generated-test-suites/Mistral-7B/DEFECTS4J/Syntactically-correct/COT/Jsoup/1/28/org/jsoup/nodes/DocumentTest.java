import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertNotNull(doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.head();
        Assert.assertNotNull(head);
        Element body = html.body();
        Assert.assertNotNull(body);
    }

    @Test
    public void testHeadAccessor() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Element newHead = doc.appendChild(doc.createElement("head"));
        Assert.assertEquals(2, doc.numberOfElements("head"));
    }

    @Test
    public void testBodyAccessor() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        Assert.assertNull(body);
        Element newBody = doc.createElement("body");
        doc.appendChild(newBody);
        Assert.assertEquals(1, doc.numberOfElements("body"));
    }

    @Test
    public void testTitleAccessor() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSetterNull() {
        Document doc = new Document("http://example.com");
        try {
            doc.title(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.outerHtml("<html><body><head><title>Test</title></head></body></html>");
        doc.normalise();
        Assert.assertEquals("<html><body><title>Test</title>" +
                "<text></text></body></html>", doc.outerHtml());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document doc = new Document("http://example.com");
        doc.normalise(null);
    }

    @Test
    public void testTextMethod() {
        Document doc = new Document("http://example.com");
        doc.outerHtml("<html><body></body></html>");
        doc.text("Test text");
        String text = doc.outerHtml();
        Assert.assertEquals("<html><body>Test text</body></html>", text);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("http://example.com");
        doc.outerHtml("<html><body></body></html>");
        String html = doc.outerHtml();
        Assert.assertEquals("<html><body></body></html>", html);
    }

    @Test
    public void testNodeNameMethod() {
        Document doc = new Document("http://example.com");
        String nodeName = doc.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
}