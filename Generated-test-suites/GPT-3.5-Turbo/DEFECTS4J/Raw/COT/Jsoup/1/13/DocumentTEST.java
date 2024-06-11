
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Assert;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document doc = new Document("https://example.com");
        Assert.assertEquals(Tag.valueOf("#root"), doc.tag());
        Assert.assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        Assert.assertEquals("https://example.com", doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("head", doc.head().tagName());
        Assert.assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://example.com");
        Assert.assertNull(doc.head());
        doc.appendElement("head");
        Assert.assertNotNull(doc.head());
        Assert.assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://example.com");
        Assert.assertNull(doc.body());
        doc.appendElement("body");
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        Assert.assertEquals("", doc.title());
        doc.head().appendElement("title").text("Example Title");
        Assert.assertEquals("Example Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("https://example.com");
        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        Assert.assertEquals("a", doc.createElement("a").tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://example.com");
        doc.normalise();
        Assert.assertNotNull(doc.select("html"));
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://example.com");
        Assert.assertEquals("", doc.outerHtml());
        doc.body().appendElement("div").text("Example");
        Assert.assertEquals("<div>Example</div>", doc.outerHtml());
    }

    @Test
    public void testSetText() {
        Document doc = new Document("https://example.com");
        doc.setText("Example Text");
        Assert.assertEquals("Example Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://example.com");
        Assert.assertEquals("#document", doc.nodeName());
    }
}

