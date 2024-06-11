package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
        Assert.assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertNull(doc.title());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();

        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());

        doc.title(null);
        Assert.assertEquals("", doc.title());

        doc.head().appendElement("meta", "name", "viewport", "content", "width=device-width, initial-scale=1.0");
        Assert.assertTrue(head.childNodes().size() > 0);

        doc.appendElement("div").text("Test Content");
        Assert.assertEquals("Test Content", body.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("head").appendElement("body").text("Test Content");
        Element p = doc.createElement("p").text("Test Text");
        doc.head().appendElement(p);

        doc.normalise();
        Assert.assertEquals(4, doc.childNodes().size());
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertEquals("Test Content", doc.body().text());
        Assert.assertNull(doc.head().childNodes().get(0));
        Assert.assertEquals("p", doc.body().childNodes().get(0).tagName());
        Assert.assertEquals("Test Text", doc.body().childNodes().get(0).text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        Document doc = new Document("http://example.com");
        doc.createElement(null);
    }
}