package org.jsoup.nodes.test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * DocumentTest.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Element html = document.head();
        Assert.assertEquals("html", html.tagName());
        Assert.assertNotNull(html.head());
        Assert.assertNotNull(html.body());
    }

    @Test
    public void testHeadAccessor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
        Assert.assertEquals("Test Title", head.child(0).text());
        document.title("");
        Assert.assertEquals("", document.title());
        Assert.assertNull(head.child(0));
    }

    @Test
    public void testBodyAccessor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        Assert.assertNull(body);
        document.title("Test Title");
        body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
        document.title("");
        Assert.assertNull(body);
    }

    @Test
    public void testTitleSetter() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
        document.title("");
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("");
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("");
        document.title("Test Title");
        document.appendElement("html").appendElement("head").text("Test Head");
        document.appendElement("html").appendElement("body").text("Test Body");
        document.normalise();
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        Assert.assertEquals("Test Title", head.child(0).text());
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
        Assert.assertTrue(body.childNodes().size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseNullElement() {
        Document document = new Document("");
        document.normalise(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element head = document.appendElement("html").appendElement("head");
        Element body = document.appendElement("html").appendElement("body");
        Element title = document.appendElement("html").appendElement("title").text("Test Title");

        Element found = document.findFirstElementByTagName("html", this);
        Assert.assertNotNull(found);
        Assert.assertEquals("html", found.tagName());

        found = document.findFirstElementByTagName("html", head);
        Assert.assertNull(found);

        found = document.findFirstElementByTagName("html", body);
        Assert.assertNull(found);

        found = document.findFirstElementByTagName("html", title);
        Assert.assertNull(found);

        found = document.findFirstElementByTagName("title", this);
        Assert.assertNotNull(found);
        Assert.assertEquals("title", found.tagName());

        found = document.findFirstElementByTagName("title", head);
        Assert.assertNull(found);

        found = document.findFirstElementByTagName("title", body);
        Assert.assertNull(found);

        found = document.findFirstElementByTagName("title", title);
        Assert.assertNotNull(found);
        Assert.assertEquals("title", found.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("");
        document.title("Test Title");
        document.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        document.appendElement("html").appendElement("body");
        String html = document.outerHtml();
        Assert.assertTrue(html.contains("Test Title"));
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("");
        document.appendElement("html").appendElement("body").appendElement("p").text("Test Text");
        document.text("Test Text Replaced");
        Assert.assertEquals("Test Text Replaced", document.text());
        Assert.assertEquals("Test Text Replaced", document.body().text());
        Assert.assertNull(document.getElementsByTag("p").first().text());
    }

    @Test
    public void testClone() {
        Document document = new Document("");
        document.title("Test Title");
        OutputSettings outputSettings = document.outputSettings();
        Document clone = document.clone();
        Assert.assertEquals(document, clone.outputSettings());
        Assert.assertNotSame(document, clone);
    }
}