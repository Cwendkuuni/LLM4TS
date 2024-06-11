import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        Assert.assertEquals("#document", document.nodeName());
        Assert.assertNotNull(document.body());
        Assert.assertNotNull(document.head());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.head();
        Assert.assertEquals("#document", html.nodeName());
        Assert.assertNotNull(html.childNodes());
        Assert.assertTrue(html.childNodes().size() > 0);
        Assert.assertEquals("html", html.tagName());

        html = document.body();
        Assert.assertEquals("#document", html.nodeName());
        Assert.assertNotNull(html.childNodes());
        Assert.assertTrue(html.childNodes().size() > 0);
        Assert.assertEquals("body", html.tagName());
    }

    @Test
    public void testHeadAccessors() {
        Document document = new Document("baseUri");
        Element head = document.head();
        Assert.assertNotNull(head);
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testBodyAccessors() {
        Document document = new Document("baseUri");
        Element body = document.body();
        Assert.assertNotNull(body);
        body.text("Test Body Text");
        String bodyText = document.body().text();
        Assert.assertEquals("Test Body Text", bodyText);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element newElement = document.createElement("a");
        Assert.assertEquals("#document", document.nodeName());
        Assert.assertEquals("a", newElement.tagName());
        Assert.assertEquals("", newElement.text());
        Assert.assertEquals("", newElement.outerHtml());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element title = document.createElement("title").text("Test Title");
        Element body = document.createElement("body").text("Test Body Text");
        Element head = document.createElement("head").appendChild(title);
        document.appendChild(head);
        document.appendChild(body);

        document.normalise();

        Element actualHead = document.head();
        Element expectedHead = document.createElement("head").appendChild(title);
        Assert.assertEquals(expectedHead, actualHead);

        Element actualBody = document.body();
        Element expectedBody = document.createElement("body").text("Test Body Text");
        Assert.assertEquals(expectedBody, actualBody);
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWithNullElement() {
        Document document = new Document("baseUri");
        document.normalise(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseWithNonElementNode() {
        Document document = new Document("baseUri");
        document.normalise(new TextNode(""));
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element htmlEl = document.appendElement("html");
        Element headEl = document.createElement("head").setText("Head Content").appendTo(htmlEl);
        Element bodyEl = document.createElement("body").setText("Body Content").appendTo(htmlEl);

        Element actualHead = document.findFirstElementByTagName("head", document);
        Element expectedHead = headEl;
        Assert.assertEquals(expectedHead, actualHead);

        Element actualBody = document.findFirstElementByTagName("body", document);
        Element expectedBody = bodyEl;
        Assert.assertEquals(expectedBody, actualBody);

        Element nonExistentElement = document.findFirstElementByTagName("nonExistentTag", document);
        Assert.assertNull(nonExistentElement);
    }

    @Test
    public void testClone() {
        Document document = new Document("baseUri");
        Element htmlEl = document.appendElement("html");
        Element headEl = document.createElement("head").setText("Head Content").appendTo(htmlEl);
        Element bodyEl = document.createElement("body").setText("Body Content").appendTo(htmlEl);

        Document clonedDocument = document.clone();

        Element clonedHtmlEl = clonedDocument.head();
        Assert.assertEquals(htmlEl, clonedHtmlEl);

        Element clonedHeadEl = clonedDocument.getElementsByTag("head").first();
        Assert.assertNotEquals(headEl, clonedHeadEl);
        Assert.assertEquals("Head Content", clonedHeadEl.text());

        OutputSettings originalOutputSettings = document.outputSettings();
        OutputSettings clonedOutputSettings = clonedDocument.outputSettings();
        Assert.assertNotSame(originalOutputSettings, clonedOutputSettings);
    }
}