package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ParserTest {
    private static final String HTML_PARSE = "<html><head><title>Test Page</title></head><body><h1>Test Heading</h1></body></html>";
    private static final String BODY_FRAGMENT = "<body><h1>Test Heading</h1></body>";

    @Test
    public void testParse() {
        // Test parsing a valid HTML
        String html = HTML_PARSE;
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check root tag
        Assert.assertEquals("html", root.tagName());

        // Check head tag
        Element head = root.child(0);
        Assert.assertEquals("head", head.tagName());

        // Check title tag
        Element title = head.child(0);
        Assert.assertEquals("title", title.tagName());
        Assert.assertEquals("Test Page", title.text());

        // Check body tag
        Element body = root.child(1);
        Assert.assertEquals("body", body.tagName());

        // Check h1 tag
        Element h1 = body.child(0);
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Test Heading", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        // Test parsing a body fragment
        String bodyHtml = BODY_FRAGMENT;
        Parser parser = new Parser(bodyHtml, "http://test.com");
        Document doc = parser.parse();

        // Check root tag
        Assert.assertEquals("html", doc.tagName());
        Element root = doc;

        // Check head tag
        Element head = root.head();
        Assert.assertNull(head);

        // Check body tag
        Element body = root.body();
        Assert.assertEquals("body", body.tagName());

        // Check h1 tag
        Element h1 = body.child(0);
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Test Heading", h1.text());
    }

    @Test
    public void testParseEmptyHtml() {
        // Test parsing an empty HTML
        String html = "";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        // Check root tag
        Assert.assertEquals("html", doc.tagName());
        Element root = doc;

        // Check child count
        Assert.assertEquals(0, root.childCount());
    }

    @Test
    public void testParseInvalidHtml() {
        // Test parsing invalid HTML
        String html = "<html<title>Test Page</title>";
        Parser parser = new Parser(html, "http://test.com");
        Assert.assertThrows(RuntimeException.class, parser::parse);
    }

    @Test
    public void testParseComment() {
        // Test parsing comments
        String html = "<html><!-- Test Comment -->";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check comment tag
        Element comment = root.child(0);
        Assert.assertEquals("!--", comment.tagName());
        Assert.assertEquals("Test Comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        // Test parsing XML declarations
        String xmlDecl = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>";
        String html = "<html>" + xmlDecl + "</html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check xml declaration tag
        XmlDeclaration xmlDeclaration = root.child(0);
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("xml", xmlDeclaration.tagName());
        Assert.assertEquals("version='1.0' encoding='UTF-8' standalone='yes'", xmlDeclaration.data());
    }

    @Test
    public void testParseEndTag() {
        // Test parsing end tags
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check p tag
        Element p = root.child(0).child(0);
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Test", p.text());

        // Check body tag
        Element body = root.child(1);
        Assert.assertEquals("body", body.tagName());

        // Check p tag is closed
        Assert.assertEquals(1, body.childCount());
    }

    @Test
    public void testParseStartTag() {
        // Test parsing start tags
        String html = "<html><h1>Test Heading</h1></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check h1 tag
        Element h1 = root.child(0);
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Test Heading", h1.text());
    }

    @Test
    public void testParseAttributes() {
        // Test parsing attributes
        String html = "<a href='http://test.com' title='Test Title' class='test-class'>Test Link</a>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check a tag
        Element a = root.child(0);
        Assert.assertEquals("a", a.tagName());
        Assert.assertEquals("Test Link", a.text());

        // Check href attribute
        Attribute href = a.attr("href");
        Assert.assertEquals("http://test.com", href.value());

        // Check title attribute
        Attribute title = a.attr("title");
        Assert.assertEquals("Test Title", title.value());

        // Check class attribute
        Attribute classes = a.attr("class");
        Assert.assertEquals("test-class", classes.value());
    }

    @Test
    public void testParseTextNode() {
        // Test parsing text nodes
        String html = "<html><body><p>Test Text</p></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check p tag
        Element p = root.child(0).child(0);
        Assert.assertEquals("p", p.tagName());

        // Check text node
        TextNode textNode = p.child(0);
        Assert.assertEquals("Test Text", textNode.text());
    }

    @Test
    public void testParseCdata() {
        // Test parsing CDATA nodes
        String html = "<html><![CDATA[<script>alert('Test');</script>]]>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check CDATA node
        TextNode cdataNode = root.child(0);
        Assert.assertEquals("<![CDATA[<script>alert('Test');</script>]]>", cdataNode.text());
    }

    @Test
    public void testParseUnclosedTag() {
        // Test parsing unclosed tags
        String html = "<html><body><p>Test<ul>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check p tag
        Element p = root.child(0);
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Test", p.text());

        // Check ul tag
        Element ul = null;
        for (Element child : p.children()) {
            if (child.tagName().equals("ul")) {
                ul = child;
                break;
            }
        }
        Assert.assertNotNull(ul);
    }

    @Test
    public void testParseEmptyTag() {
        // Test parsing empty tags
        String html = "<html><body><img/></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check img tag
        Element img = root.child(0).child(0);
        Assert.assertEquals("img", img.tagName());
        Assert.assertFalse(img.text().isEmpty());
    }

    @Test
    public void testParseBaseTag() {
        // Test parsing base tags
        String html = "<html><head><base href='http://test.com'>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check base tag
        Element base = root.head().child(0);
        Assert.assertEquals("base", base.tagName());
        Assert.assertEquals("http://test.com", base.attr("href").value());
    }

    @Test
    public void testParseImplicitParent() {
        // Test parsing implicit parents
        String html = "<html><body><script>alert('Test');</script></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();
        Element root = doc;

        // Check script tag
        Element script = root.child(0).child(0);
        Assert.assertEquals("script", script.tagName());
        Assert.assertNotNull(script.parent());

        // Check parent tag
        Element parent = script.parent();
        Assert.assertEquals("body", parent.tagName());
    }
}