package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUrl");
        Assert.assertNotNull(doc);
        Assert.assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUrl");
        Assert.assertNotNull(doc);
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.nodeName());
        Element head = html.head();
        Assert.assertNotNull(head);
        Element body = html.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("baseUrl");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("baseUrl");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitleMethod() {
        Document doc = new Document("baseUrl");
        String title = doc.title();
        Assert.assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSetter() {
        Document doc = new Document("baseUrl");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("baseUrl");
        Element element = doc.createElement("a");
        Assert.assertNotNull(element);
        Assert.assertEquals("a", element.nodeName());
        Assert.assertEquals("baseUrl", element.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("baseUrl");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        body.appendText("Test Body");
        Element textNode = doc.createElement("textnode");
        textNode.text("Test Text Node");
        body.prependChild(textNode);

        doc.normalise();

        Element foundTitle = doc.getElementsByTag("title").first();
        Assert.assertNotNull(foundTitle);
        Assert.assertEquals("Test Title", foundTitle.text());
        Element foundTextNode = body.getElementsByTag("textnode").first();
        Assert.assertNotNull(foundTextNode);
        Assert.assertEquals("Test Text Node", foundTextNode.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriConstructor() {
        new Document(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagNameCreateElement() {
        Document doc = new Document("baseUrl");
        doc.createElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagNameCreateElement() {
        Document doc = new Document("baseUrl");
        doc.createElement("invalidTagName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriCreateShell() {
        Document.createShell(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUrl");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element p = body.appendElement("p");
        p.text("Test Text");

        Element foundTitle = Document.findFirstElementByTagName("title", doc);
        Assert.assertNotNull(foundTitle);
        Assert.assertEquals("Test Title", foundTitle.text());

        Element foundP = Document.findFirstElementByTagName("p", doc);
        Assert.assertNotNull(foundP);
        Assert.assertEquals("Test Text", foundP.text());

        Element notFound = Document.findFirstElementByTagName("div", doc);
        Assert.assertNull(notFound);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = new Document("baseUrl");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        body.appendText("Test Body");

        String htmlString = doc.outerHtml();
        String expectedHtmlString = "<html><head><title>Test Title</title></head><body>Test Body</body></html>";
        Assert.assertEquals(expectedHtmlString, htmlString);
    }

    @Test
    public void testTextMethod() {
        Document doc = new Document("baseUrl");
        Element body = doc.body();
        body.text("Test Body");

        String text = doc.text();
        String expectedText = "Test Body";
        Assert.assertEquals(expectedText, text);
    }
}