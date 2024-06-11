import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.child(0);
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        Element body = html.child(1);
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        Assert.assertNotNull(element);
        Assert.assertEquals("a", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        TextNode textNode1 = document.appendTextNode("Test 1", "#test1");
        TextNode textNode2 = document.appendTextNode("Test 2", "#test2");
        textNode1.insertBefore(textNode2);
        document.normalise();
        Assert.assertSame(body.child(0), textNode1);
        Assert.assertSame(body.child(1), textNode2);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element element = document.findFirstElementByTagName("body", this.document);
        Assert.assertSame(body, element);
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        body.appendText("Test");
        Assert.assertEquals("<html><head></head><body>Test</body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");
        body.appendText("Test");
        Assert.assertEquals("Test", document.text());
    }

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }
}