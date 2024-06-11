package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
        Assert.assertNull(document.title());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        Assert.assertEquals("#html", html.nodeName());
        Element head = html.child(0);
        Assert.assertEquals("#head", head.nodeName());
        Element body = html.child(1);
        Assert.assertEquals("#body", body.nodeName());
    }

    @Test
    public void testSetAndGetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleWithNull() {
        Document document = new Document("http://example.com");
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        Assert.assertEquals("#div", element.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = document.appendElement("title", "Test Title");
        text(body, "Test Body");
        text(document, "Test Document");

        document.normalise();

        Element body2 = document.getElementsByTag("body").first();
        Assert.assertEquals("Test Body", body2.text());
        Element title2 = document.getElementsByTag("title").first();
        Assert.assertEquals("Test Title", title2.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        Element headTitle = document.findFirstElementByTagName("title", document);
        Assert.assertEquals("#title", headTitle.nodeName());
        Element bodyDiv = document.findFirstElementByTagName("div", body);
        Assert.assertNull(bodyDiv);
    }

    @Test
    public void testSetOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings settings = document.outputSettings();
        settings.charset("UTF-16");
        Assert.assertEquals("UTF-16", settings.charset().name());
    }

    @Test
    public void testGetDocumentTextWithPrettyPrint() {
        Document document = new Document("http://example.com");
        document.outputSettings().prettyPrint(true);
        text(document, "<html><head><title>Test Title</title></head><body><p>Test Text</p></body></html>");
        Assert.assertEquals("<html>\n<head>\n<title>Test Title</title>\n</head>\n<body>\n<p>Test Text</p>\n</body>\n</html>", document.outerHtml());
    }

    @Test
    public void testGetDocumentTextWithoutPrettyPrint() {
        Document document = new Document("http://example.com");
        document.outputSettings().prettyPrint(false);
        text(document, "<html><head><title>Test Title</title></head><body><p>Test Text</p></body></html>");
        Assert.assertEquals("<html><head><title>Test Title</title></head><body><p>Test Text</p></body></html>", document.outerHtml());
    }

    private void text(Node node, String text) {
        Element element = node instanceof Element ? (Element) node : new Element(Tag.valueOf("#text"), node.baseUri());
        element.text(text);
        node.appendChild(element);
    }

    private void text(Document document, String text) {
        Element body = document.body();
        text(body, text);
    }
}