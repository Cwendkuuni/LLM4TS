package org.jsoup.parser.test;

import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class ParserTest {

    @org.junit.Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        Assert.assertNull(root);
    }

    @org.junit.Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        Assert.assertNotNull(root);
        Assert.assertEquals("Hello World", root.child(0).text());
        Assert.assertEquals("Test", doc.title().text());
    }

    @org.junit.Test
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test</title><body><script>alert('Hello World')</script></body></html>";
        Document doc;
        try {
            doc = Parser.parse(html, "http://example.com");
        } catch (RuntimeException e) {
            doc = null;
        }
        Element root = doc;
        Assert.assertNull(root);
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element root = doc.body();
        Assert.assertNotNull(root);
        // cannot test script contents directly, but we know it's present
    }

    @org.junit.Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Comment comment = doc.body().child(0).nextSibling();
        Assert.assertNotNull(comment);
        Assert.assertEquals("This is a comment", comment.text());
    }

    @org.junit.Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = doc.body().child(0);
        Assert.assertNotNull(xmlDecl);
        Assert.assertEquals("UTF-8", xmlDecl.encoding());
    }

    @org.junit.Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World<br /></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().child(0);
        p.remove();
        Assert.assertEquals(1, doc.body().childSize());
    }

    @org.junit.Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1 id='header'>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element h1 = doc.body().child(0);
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Hello World", h1.text());
        Assert.assertNotNull(h1.id());
        Assert.assertEquals("header", h1.id());
    }

    @org.junit.Test
    public void testParseAttribute() {
        String html = "<a href='http://example.com' class='link' title='Test' target='_blank'>Link</a>";
        Document doc = Parser.parse(html, "http://example.com");
        Element a = doc.body().child(0);
        Assert.assertEquals("a", a.tagName());
        Assert.assertEquals("Link", a.text());
        Assert.assertEquals("http://example.com", a.attr("href"));
        Assert.assertEquals("link", a.attr("class"));
        Assert.assertEquals("Test", a.attr("title"));
        Assert.assertEquals("_blank", a.attr("target"));
    }

    @org.junit.Test
    public void testParseTextNode() {
        String html = "<p>This is a text node</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().child(0);
        TextNode textNode = (TextNode) p.child(0);
        Assert.assertEquals("This is a text node", textNode.text());
    }

    @org.junit.Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ <script>alert('Hello World');</script> ]]><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element script = doc.body().child(0).child(0);
        Assert.assertEquals("script", script.tagName());
        Assert.assertFalse(script.text().isEmpty());
    }

    @org.junit.Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/subdir/'></head><body><a href='page.html'>Go to page</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Assert.assertEquals("/subdir/page.html", doc.baseUri());
        Assert.assertEquals("/subdir/page.html", doc.getElementById("link").absUrl("href"));
    }

    @org.junit.Test
    public void testParseImplicitParent() {
        String html = "<html><body><img src='image.png'></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.body().child(0);
        Assert.assertEquals("img", img.tagName());
        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        div.appendChild(img);
        Element parent = addChildToParent(div, false);
        Assert.assertEquals(1, parent.childSize());
    }

    @org.junit.Test
    public void testParseInvalidTagOrder() {
        String html = "<html><body><head><title>Test</title></head></body></html>";
        try {
            Parser.parse(html, "http://example.com");
            Assert.fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseSelfClosingTag() {
        String html = "<html><head><title>Test</title></head><body><img src='image.png' /></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.body().child(0);
        Assert.assertEquals("img", img.tagName());
        Assert.assertTrue(img.isSelfClosing());
    }

    private Element addChildToParent(Element child, boolean isEmptyElement) {
        Document doc = Jsoup.parse("<html><body></body></html>");
        Element parent = doc.body();
        doc.body().remove();
        doc.appendChild(parent);
        parent.appendChild(child);
        return parent;
    }
}