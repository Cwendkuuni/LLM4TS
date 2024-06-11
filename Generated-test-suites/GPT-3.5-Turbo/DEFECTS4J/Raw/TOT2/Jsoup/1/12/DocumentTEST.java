import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "www.example.com";
        Document doc = Document.createShell(baseUri);
        Assert.assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        Assert.assertNotNull(html);
        Element head = doc.head();
        Assert.assertNotNull(head);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testHead() {
        Document doc = new Document("www.example.com");
        Element head = doc.head();
        Assert.assertNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("www.example.com");
        Element body = doc.body();
        Assert.assertNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("www.example.com");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("www.example.com");
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("www.example.com");
        Element element = doc.createElement("div");
        Assert.assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("www.example.com");
        doc.normalise();
        Assert.assertNotNull(doc.getElementsByTag("html").first());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("www.example.com");
        Assert.assertEquals("", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("www.example.com");
        doc.text("Test text");
        Assert.assertEquals("Test text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("www.example.com");
        Assert.assertEquals("#document", doc.nodeName());
    }
}