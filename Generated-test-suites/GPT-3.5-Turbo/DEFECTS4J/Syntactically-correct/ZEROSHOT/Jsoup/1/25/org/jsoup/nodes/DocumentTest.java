package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        Document document = new Document("http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals(Tag.valueOf("#root"), document.tag());
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertNotNull(document);

        Element htmlElement = document.getElementsByTag("html").first();
        Assert.assertNotNull(htmlElement);

        Element headElement = htmlElement.getElementsByTag("head").first();
        Assert.assertNotNull(headElement);

        Element bodyElement = htmlElement.getElementsByTag("body").first();
        Assert.assertNotNull(bodyElement);
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://example.com");
        Element headElement = document.head();
        Assert.assertNotNull(headElement);
        Assert.assertEquals("head", headElement.tagName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://example.com");
        Element bodyElement = document.body();
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals("body", bodyElement.tagName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleWithNoTitleElement() {
        Document document = Document.createShell("http://example.com");
        String title = document.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        Document document = Document.createShell("http://example.com");
        document.head().appendElement("title").text("Existing Title");
        String title = document.title();
        Assert.assertEquals("Existing Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.body().text("This is some text");
        document.normalise();
        Element bodyElement = document.body();
        Assert.assertEquals(1, bodyElement.childNodes.size());
        Assert.assertTrue(bodyElement.childNode(0) instanceof TextNode);
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        String outerHtml = document.outerHtml();
        Assert.assertEquals(document.html(), outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.text("This is some text");
        Element bodyElement = document.body();
        Assert.assertEquals("This is some text", bodyElement.text());
        Assert.assertTrue(document.html().contains("This is some text"));
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("http://example.com");
        String nodeName = document.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
}