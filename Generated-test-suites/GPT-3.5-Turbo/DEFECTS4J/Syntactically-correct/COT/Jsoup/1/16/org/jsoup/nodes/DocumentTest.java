
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testConstructor() {
        // Test case for Document constructor
        Document document = new Document("https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        // Test case for createShell method
        Document document = Document.createShell("https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals("html", document.getElementsByTag("html").first().nodeName());
        Assert.assertEquals("head", document.head().nodeName());
        Assert.assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testHead() {
        // Test case for head method
        Document document = Document.createShell("https://www.example.com");
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        // Test case for body method
        Document document = Document.createShell("https://www.example.com");
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        // Test case for title method
        Document document = Document.createShell("https://www.example.com");
        Assert.assertEquals("", document.title());

        document.title("Example Title");
        Assert.assertEquals("Example Title", document.title());

        document.head().appendElement("title").text("New Title");
        Assert.assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleException() {
        // Test case for title method exception handling
        Document document = Document.createShell("https://www.example.com");

        try {
            document.title(null);
            Assert.fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            Assert.assertEquals("title must not be null", e.getMessage());
        }
    }

    @Test
    public void testCreateElement() {
        // Test case for createElement method
        Document document = Document.createShell("https://www.example.com");
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        // Test case for normalise method
        Document document = Document.createShell("https://www.example.com");
        document.normalise();

        Assert.assertEquals("html", document.getElementsByTag("html").first().nodeName());
        Assert.assertEquals("head", document.head().nodeName());
        Assert.assertEquals("body", document.body().nodeName());

        Element head = document.head();
        Assert.assertEquals(1, head.childNodes.size()); // One TextNode should be added
        Assert.assertEquals(" ", head.childNodes.get(0).outerHtml());

        Element body = document.body();
        Assert.assertEquals(0, body.childNodes.size()); // No TextNode should be in body
    }

    @Test
    public void testText() {
        // Test case for text method
        Document document = Document.createShell("https://www.example.com");
        document.text("Example Text");
        Assert.assertEquals("Example Text", document.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        // Test case for outerHtml method
        Document document = Document.createShell("https://www.example.com");
        Assert.assertEquals("", document.outerHtml());

        document.createElement("div");
        Assert.assertEquals("<div></div>", document.outerHtml());
    }
}
