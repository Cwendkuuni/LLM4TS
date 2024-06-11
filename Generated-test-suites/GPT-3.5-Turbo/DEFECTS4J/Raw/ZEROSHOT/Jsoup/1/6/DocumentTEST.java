import org.junit.Test;
import org.junit.Assert;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.getElementsByTag("html"));
        Assert.assertNotNull(doc.getElementsByTag("head"));
        Assert.assertNotNull(doc.getElementsByTag("body"));
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        doc.appendElement("head");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        doc.appendElement("body");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.appendElement("head").appendElement("title").text("Example Title");
        String title = doc.title();
        Assert.assertEquals("Example Title", title);

        doc.title("New Title");
        title = doc.title();
        Assert.assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        TextNode textNode = new TextNode("Example Text", "");
        doc.appendChild(textNode);

        doc.normalise();

        Assert.assertTrue(doc.select("html").size() > 0);
        Assert.assertTrue(doc.head() != null);
        Assert.assertTrue(doc.body() != null);

        Assert.assertFalse(html.childNodes.contains(textNode));
        Assert.assertTrue(body.childNodes.contains(textNode));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html");
        doc.appendElement("div").text("Example Text");

        String outerHtml = doc.outerHtml();

        Assert.assertTrue(outerHtml.contains("<html>"));
        Assert.assertTrue(outerHtml.contains("<div>Example Text</div>"));
        Assert.assertFalse(outerHtml.contains("#root"));
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");

        doc.text("Example Text");

        Assert.assertEquals("Example Text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        String nodeName = doc.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
}