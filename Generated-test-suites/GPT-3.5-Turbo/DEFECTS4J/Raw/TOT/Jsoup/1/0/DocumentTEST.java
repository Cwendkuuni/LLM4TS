package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

import java.util.List;
import java.util.ArrayList;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://www.example.com");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("html", doc.body().parentNode().nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("html", doc.body().parentNode().nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testNullTitle() {
        Document doc = new Document("http://www.example.com");
        doc.title(null);
        String title = doc.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testTitleElement() {
        Document doc = new Document("http://www.example.com");
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertNull(titleEl);
        doc.title("Test Title");
        titleEl = doc.getElementsByTag("title").first();
        Assert.assertNotNull(titleEl);
        Assert.assertEquals("title", titleEl.nodeName());
        Assert.assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        Element element = doc.createElement("div");
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals("http://www.example.com", element.baseUri());
        Assert.assertEquals(doc, element.ownerDocument());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        doc.createElement("div");
        doc.createElement("span");
        doc.normalise();
        Element body = doc.body();
        List<Node> children = body.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals("#root", children.get(0).nodeName());
        Assert.assertEquals(" ", children.get(1).outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        doc.createElement("div");
        doc.createElement("span");
        String outerHtml = doc.outerHtml();
        Assert.assertEquals("<div></div><span></span>", outerHtml);
    }

    @Test
    public void testSetText() {
        Document doc = new Document("http://www.example.com");
        doc.createElement("div");
        doc.setText("Test Text");
        String text = doc.body().text();
        Assert.assertEquals("Test Text", text);
    }

}