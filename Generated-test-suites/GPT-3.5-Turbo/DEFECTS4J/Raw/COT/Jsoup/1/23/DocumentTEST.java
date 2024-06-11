
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("html", document.getElementsByTag("html").first().nodeName());
        Assert.assertNotNull(document.getElementsByTag("head").first());
        Assert.assertNotNull(document.getElementsByTag("body").first());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        Element head = document.appendElement("head");
        Assert.assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");
        Assert.assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        Element titleElement = document.appendElement("title");
        titleElement.text("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("New Title");
        Assert.assertEquals("New Title", document.getElementsByTag("title").first().text());
        Assert.assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        Assert.assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element div = body.appendElement("div");
        div.text("Test");

        document.normalise();

        Assert.assertEquals("html", document.getElementsByTag("html").first().nodeName());
        Assert.assertEquals(head, document.head());
        Assert.assertEquals(body, document.body());
        Assert.assertEquals(1, document.body().childNodes.size());
        Assert.assertEquals(div, document.body().childNodes.get(0));
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element body = html.appendElement("body");
        Element div = body.appendElement("div");
        div.text("Test");

        Assert.assertEquals("<div>Test</div>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        Element body = document.appendElement("body");

        document.text("Test");

        Assert.assertEquals("Test", body.text());
    }

}
