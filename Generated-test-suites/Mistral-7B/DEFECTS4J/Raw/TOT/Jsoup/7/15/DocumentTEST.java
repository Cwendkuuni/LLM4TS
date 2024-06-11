package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNull(document.title());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Element html = document.head();
        Assert.assertEquals("html", html.tagName());
        Element head = document.head();
        Assert.assertEquals("head", head.tagName());
        Element body = document.body();
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testSetAndGetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
        document.title(null);
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testFindElementsByTagName() {
        Document document = new Document("http://example.com");
        Element title = document.createElement("title");
        Element body = document.createElement("body");
        document.appendChildren(title, body);

        List<Element> elements = document.getElementsByTagName("title");
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(title, elements.get(0));

        elements = document.getElementsByTagName("body");
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(body, elements.get(0));
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element title = document.createElement("title");
        Element body = document.createElement("body");
        document.appendChildren(title, body);

        Element foundTitle = document.findFirstElementByTagName("title", document);
        Element foundBody = document.findFirstElementByTagName("body", document);

        Assert.assertEquals(title, foundTitle);
        Assert.assertEquals(body, foundBody);

        Element notFound = document.findFirstElementByTagName("non-existent", document);
        Assert.assertNull(notFound);
    }

    @Test
    public void testNormalize() {
        Document document = new Document("http://example.com");
        document.text("Text before");
        Element title = document.createElement("title");
        title.text("Title text");
        document.appendChild(title);

        document.normalize();

        Assert.assertEquals("Title text", document.title());
        Assert.assertEquals("Text before", document.body().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTitle() {
        Document document = new Document("http://example.com");
        document.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        Document document = new Document("http://example.com");
        document.title("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Document document = new Document(null);
    }
}