import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertNotNull(document);
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.nodeName().toLowerCase());
        Element head = html.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName().toLowerCase());
        Element body = html.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName().toLowerCase());
    }

    @Test
    public void testGetHead() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head");
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName().toLowerCase());
    }

    @Test
    public void testGetBody() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("body");
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName().toLowerCase());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title.trim());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head");
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title.trim());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleNull() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element element = document.createElement("a");
        Assert.assertNotNull(element);
        Assert.assertEquals("a", element.nodeName().toLowerCase());
        Assert.assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head").appendText("Test");
        document.appendElement("html").appendElement("body").appendText("Test");
        document.normalise();
        Element head = document.head();
        Assert.assertNotNull(head);
        Element body = document.body();
        Assert.assertNotNull(body);
        TextNode headTextNode = (TextNode) head.text();
        TextNode bodyTextNode = (TextNode) body.text();
        Assert.assertEquals(0, head.childNodes().size());
        Assert.assertEquals(0, body.childNodes().size());
        Assert.assertEquals("Test", headTextNode.text());
        Assert.assertEquals("Test", bodyTextNode.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        Element title = document.findFirstElementByTagName("title", document);
        Assert.assertNotNull(title);
        Assert.assertEquals("title", title.nodeName().toLowerCase());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        String html = document.outerHtml();
        Assert.assertTrue(html.contains("<html>") && html.contains("<head>") && html.contains("<title>Test Title</title>"));
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.appendElement("html").appendElement("body").text("Test Text");
        String text = document.text("Test Text");
        Assert.assertEquals("Test Text", text.trim());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element element = document.createElement("a").text("Test");
        Document clonedDocument = document.clone();
        Element clonedElement = (Element) clonedDocument.getElementById("root").getElementsByTag("a").first();
        Assert.assertEquals("Test", clonedElement.text().trim());
    }
}