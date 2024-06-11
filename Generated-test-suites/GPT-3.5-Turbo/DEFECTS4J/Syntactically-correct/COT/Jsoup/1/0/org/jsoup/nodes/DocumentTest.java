
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("https://example.com");

        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://example.com");

        Assert.assertEquals("html", document.getElementsByTag("html").first().tagName());
        Assert.assertEquals("head", document.getElementsByTag("head").first().tagName());
        Assert.assertEquals("body", document.getElementsByTag("body").first().tagName());
        Assert.assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testHeadAccessor() {
        Document document = Document.createShell("https://example.com");
        Element head = document.head();

        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyAccessor() {
        Document document = Document.createShell("https://example.com");
        Element body = document.body();

        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("https://example.com");

        Assert.assertEquals("", document.title());

        document.title("Test Title");

        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("https://example.com");
        Element element = document.createElement("div");

        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://example.com");
        Element element = document.createElement("div");
        element.text("Test Text");
        document.append(element);

        document.normalise();

        Assert.assertEquals("", element.text());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("https://example.com");

        Assert.assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("https://example.com");

        document.text("Test Text");

        Assert.assertEquals("Test Text", document.body().text());
    }
}

