package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1><p>World</p></body></html>";
        parser = new Parser(html, null, false);
    }

    

    @Test
    public void testParse() {
        Document doc = parser.parse();
        Element root = doc.body();

        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("head", root.child(0).tagName());
        Assert.assertEquals("title", root.child(0).child(0).tagName());
        Assert.assertEquals("Hello", root.child(1).tagName());
        Assert.assertEquals("h1", root.child(1).tagName());
        Assert.assertEquals("World", root.child(1).child(0).text());
        Assert.assertEquals("p", root.child(2).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        Element root = doc.body();

        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("body", root.child(0).tagName());
        Assert.assertEquals("p", root.child(0).tagName());
        Assert.assertEquals("World", root.child(0).child(0).text());
        Assert.assertEquals(0, doc.head().childCount());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String bodyHtml = "<div><p>World</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null, true);
        Element root = doc.body();

        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("body", root.child(0).tagName());
        Assert.assertEquals("div", root.child(0).child(0).tagName());
        Assert.assertEquals("p", root.child(0).child(0).child(0).tagName());
        Assert.assertEquals("World", root.child(0).child(0).child(0).child(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser parser = new Parser(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser parser = new Parser("<html></html>", null, false);
    }

    @Test
    public void testParseComment() {
        parser.tq.consume("<!-- This is a comment -->");
        Comment comment = parser.last().child(0);
        Assert.assertEquals("<!-- This is a comment -->", comment.data());
    }

    @Test
    public void testParseXmlDecl() {
        parser.tq.consume("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        XmlDeclaration xmlDeclaration = parser.last().child(0);
        Assert.assertFalse(parser.parserIsRelaxed());
        Assert.assertEquals("html", xmlDeclaration.namespaceUri());
        Assert.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", xmlDeclaration.publicId());
        Assert.assertEquals("XHTML 1.0 Transitional//EN", xmlDeclaration.systemId());
    }

    @Test
    public void testParseEndTag() {
        parser.tq.consume("</p>");
        Assert.assertEquals("p", parser.last().tagName());
    }

    @Test
    public void testParseStartTag() {
        parser.tq.consume("<div>");
        Element div = parser.last();
        Assert.assertEquals("div", div.tagName());

        parser.tq.consume("<img src=\"test.jpg\" alt=\"Test\">");
        Element img = parser.last();
        Assert.assertEquals("img", img.tagName());
        Assert.assertEquals("test.jpg", img.absUrl("src"));
        Assert.assertEquals("Test", img.attr("alt"));

        parser.tq.consume("<input type=\"text\">");
        Element input = parser.last();
        Assert.assertEquals("input", input.tagName());
        Assert.assertEquals("text", input.attr("type"));

        parser.tq.consume("<input type=\"text\" name=\"name\">");
        Element inputWithName = parser.last();
        Assert.assertEquals("input", inputWithName.tagName());
        Assert.assertEquals("text", inputWithName.attr("type"));
        Assert.assertEquals("name", inputWithName.attr("name"));

        parser.tq.consume("<base href=\"/test\">");
        Assert.assertEquals("/test", parser.baseUri);
        Assert.assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testParseAttribute() {
        parser.tq.consume("<a href=\"test.jpg\">Test</a>");
        Element a = parser.last();
        Assert.assertEquals("a", a.tagName());
        Assert.assertEquals("test.jpg", a.absUrl("href"));

        parser.tq.consume("<p id=\"test\" class=\"test\">Test</p>");
        Element p = parser.last();
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("test", p.attr("id"));
        Assert.assertEquals("test", StringUtils.join(p.classNames(), " "));
    }

    @Test
    public void testParseTextNode() {
        parser.tq.consume("Test");
        TextNode textNode = parser.last();
        Assert.assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseCdata() {
        parser.tq.consume("<![CDATA[Test]]>");
        TextNode textNode = parser.last();
        Assert.assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseImplicitParent() {
        parser.tq.consume("<img src=\"test.jpg\">");
        Element img = parser.last();
        Assert.assertEquals("img", img.tagName());
        Assert.assertEquals("img", img.parent().tagName());
        Assert.assertEquals("body", img.parent().parent().tagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidTagOrder() {
        parser.tq.consume("<p><p>Test</p>");
    }

    @Test
    public void testParseSelfClosingTag() {
        parser.tq.consume("<img src=\"test.jpg\" />");
        Element img = parser.last();
        Assert.assertEquals("img", img.tagName());
        Assert.assertTrue(img.isSelfClosing());
        Assert.assertEquals("img", img.parent().tagName());
        Assert.assertEquals("body", img.parent().parent().tagName());
    }

    @Test
    public void testParseUnknownTag() {
        parser.tq.consume("<unknown>");
        Element unknown = parser.last();
        Assert.assertEquals("unknown", unknown.tagName());
    }

    @Test
    public void testParseInvalidBaseUri() {
        parser = new Parser("<html><base href=\"test:\" /><a href=\"link.html\"></a></html>", null, false);
        Assert.assertEquals("", parser.baseUri);
    }

    @Test
    public void testParseNoBaseUri() {
        parser = new Parser("<html><a href=\"link.html\"></a></html>", null, false);
        Assert.assertEquals("", parser.baseUri);
    }

    @Test
    public void testParseValidBaseUri() {
        String baseUri = "http://test.com";
        parser = new Parser("<html><base href=\"" + baseUri + "\" /><a href=\"link.html\"></a></html>", baseUri, false);
        Assert.assertEquals(baseUri, parser.baseUri);
    }

    @Test
    public void testParseValidBaseUriWithSlash() {
        String baseUri = "http://test.com/";
        parser = new Parser("<html><base href=\"" + baseUri + "\" /><a href=\"link.html\"></a></html>", baseUri, false);
        Assert.assertEquals(baseUri, parser.baseUri);
    }

    @Test
    public void testParseValidBaseUriWithTrailingSlash() {
        String baseUri = "http://test.com/";
        parser = new Parser("<html><base href=\"" + baseUri + "/\" /><a href=\"link.html\"></a></html>", baseUri, false);
        Assert.assertEquals(baseUri, parser.baseUri);
    }

    
}