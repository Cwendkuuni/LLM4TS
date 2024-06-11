package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class DocumentTest {

    @Test
    public void testEmptyDocumentCreation() {
        Document doc = new Document("");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testShellDocumentCreation() {
        Document doc = Document.createShell("");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("html", doc.head().parentNode().nodeName());
        Assert.assertEquals("html", doc.body().parentNode().nodeName());
    }

    @Test
    public void testTitleAccessor() {
        Document doc = new Document("");
        Assert.assertEquals("", doc.title());

        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testElementCreation() {
        Document doc = new Document("");
        Element element = doc.createElement("div");
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("");
        doc.body().text("Text 1");
        doc.head().text("Text 2");
        doc.normalise();

        Element body = doc.body();
        Assert.assertEquals(3, body.childNodes.size());
        Assert.assertTrue(body.childNodes.get(0) instanceof TextNode);
        Assert.assertEquals(" ", ((TextNode) body.childNodes.get(0)).text());
        Assert.assertTrue(body.childNodes.get(1) instanceof TextNode);
        Assert.assertEquals("Text 1", ((TextNode) body.childNodes.get(1)).text());
        Assert.assertTrue(body.childNodes.get(2) instanceof TextNode);
        Assert.assertEquals("Text 2", ((TextNode) body.childNodes.get(2)).text());
    }

    @Test
    public void testTextSetter() {
        Document doc = new Document("");
        doc.text("Test Text");
        Assert.assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("");
        doc.body().appendElement("div").text("Test");
        String html = doc.outerHtml();
        Assert.assertNotEquals("<html><head></head><body><div>Test</div></body></html>", html);
        Assert.assertEquals("<div>Test</div>", html);
    }

}