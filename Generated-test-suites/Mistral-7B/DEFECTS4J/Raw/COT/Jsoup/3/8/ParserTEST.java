package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.jsoup.parser.Parser.*;

/**
 * Test cases for Parser class.
 */
public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();

        Assert.assertEquals("html", root.tagName());
        Element title = root.head();
        Assert.assertEquals("head", title.tagName());
        Element h1 = root.getElementsByTag("h1").first();
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello, Body!</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element root = doc;

        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals(0, root.head().children().size());
        Element h1 = root.getElementsByTag("h1").first();
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Hello, Body!", h1.text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals(0, root.childElements().size());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<title>Test</html>";
        try {
            Document doc = Parser.parse(html, "http://example.com");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expect an IllegalArgumentException due to invalid HTML
        }
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        XmlDeclaration xmlDecl = root.getElementsByTag("xml").first();
        Assert.assertNotNull(xmlDecl);
        Assert.assertEquals("xml", xmlDecl.tagName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.xmlLang());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        Comment comment = root.getElementsByTag("comment").first();
        Assert.assertNotNull(comment);
        Assert.assertEquals("comment", comment.tagName());
        Assert.assertEquals("Test Comment", comment.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        Element p = root.getElementsByTag("p").first();
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Hello, World!", p.text());

        Parser parser = new Parser(html.substring(html.indexOf("</body>")), "http://example.com", true);
        doc = parser.parse();
        p = root.getElementsByTag("p").first();
        Assert.assertEquals("p", p.tagName());
        Assert.assertNull(p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p class=\"test\"><a href=\"http://example.com\">Link</a></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        Element p = root.getElementsByTag("p").first();
        Assert.assertEquals("p", p.tagName());

        Element a = p.getElementsByTag("a").first();
        Assert.assertEquals("a", a.tagName());
        Assert.assertEquals("Link", a.text());
        Assert.assertEquals("http://example.com", a.absUrl("href"));

        Map<String, String> attributes = new HashMap<>();
        attributes.put("class", "test");
        Assert.assertEquals(attributes, p.attributes());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        Element p = root.getElementsByTag("p").first();
        Assert.assertEquals("p", p.tagName());
        TextNode textNode = (TextNode) p.child(0);
        Assert.assertEquals("Hello, World!", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Test CDATA]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc;

        TextNode textNode = (TextNode) root.getElementsByTag("cdata").first();
        Assert.assertEquals("Test CDATA", textNode.text());
    }

    @Test
    public void testParseAttributeWithSingleQuoteValue() {
        String html = "<input type=\"text\" name=\"name\" value=\"John Doe'\" />";
        Document doc = Parser.parse(html, "http://example.com");
        Element input = doc.getElementsByTag("input").first();
        Assert.assertEquals("John Doe'", input.attr("value"));
    }

    @Test
    public void testParseAttributeWithDoubleQuoteValue() {
        String html = "<input type=\"text\" name=\"name\" value=\"John Doe\" />";
        Document doc = Parser.parse(html, "http://example.com");
        Element input = doc.getElementsByTag("input").first();
        Assert.assertEquals("John Doe", input.attr("value"));
    }

    @Test
    public void testParseAttributeWithWhitespaceValue() {
        String html = "<div id=\"myDiv\"><span class=\"myClass\"> Test Attribute </span></div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element div = doc.getElementsByTag("div").first();
        Assert.assertEquals("myDiv", div.attr("id"));
        Assert.assertEquals("Test Attribute", div.getElementsByTag("span").first().text());
    }

    @Test
    public void testParseAttributeWithEmptyValue() {
        String html = "<div id=\"myDiv\"></div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element div = doc.getElementsByTag("div").first();
        Assert.assertEquals("", div.attr("id"));
    }

    @Test
    public void testParseImplicitParentTag() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.getElementsByTag("img").first();
        Assert.assertEquals("img", img.tagName());

        Element parent = img.parent();
        Assert.assertEquals("div", parent.tagName()); // an implicit "div" tag is created around the "img" tag
    }

    @Test
    public void testParseInvalidAttributeValue() {
        String html = "<input type=\"text\" name=\"name\" value=\"<script>alert('XSS');</script>\">";
        try {
            Document doc = Parser.parse(html, "http://example.com");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expect an IllegalArgumentException due to invalid attribute value
        }
    }

    @Test
    public void testParseInvalidAttributeName() {
        String html = "<input type=\"text\" name=\"name\" value=\"Value\" onfocus=\"alert('Focus');\">";
        Document doc = Parser.parse(html, "http://example.com");
        Element input = doc.getElementsByTag("input").first();

        Assert.assertFalse(input.hasAttr("onfocus")); // The "onfocus" attribute is not parsed as a regular attribute
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/subdir/\"><title>Test</title></head><body><a href=\"foo.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        Assert.assertEquals("/subdir/foo.html", doc.baseUri());
        Assert.assertEquals("Link", doc.getElementsByTag("a").first().text());
    }
}