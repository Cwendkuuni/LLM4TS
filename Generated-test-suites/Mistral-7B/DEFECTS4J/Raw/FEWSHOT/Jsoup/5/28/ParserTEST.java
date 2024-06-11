package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc;
        Element headNode = htmlNode.getElementsByTag("head").get(0);
        Element titleNode = headNode.getElementsByTag("title").get(0);
        Element bodyNode = htmlNode.getElementsByTag("body").get(0);
        Element h1Node = bodyNode.getElementsByTag("h1").get(0);

        assertEquals(htmlTag, htmlNode.tag());
        assertEquals(headTag, headNode.tag());
        assertEquals(titleTag, titleNode.tag());
        assertEquals(bodyTag, bodyNode.tag());
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Test", titleNode.text());
        assertEquals("Hello World", h1Node.text());

        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(html, "http://test.com");
        Element bodyNode = doc.body();
        Element h1Node = bodyNode.getElementsByTag("h1").get(0);

        assertEquals(bodyTag, bodyNode.tag());
        assertEquals("http://test.com", doc.baseUri());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<div id=\"foo\">Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");
        Element divNode = doc.body().getElementsByTag("div").get(0);

        assertEquals("http://test.com", doc.baseUri());
        assertEquals("Hello World", divNode.text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals(htmlTag, doc.tag());
        assertTrue(doc.getElementsByTag("*").isEmpty());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "http://test.com");
        Comment comment = doc.getElementsByTag("comment").get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        Document doc = Parser.parse(html, "http://test.com");
        XmlDeclaration xmlDecl = doc.getElementsByTag("xml-declaration").get(0);
        assertEquals("1.0", xmlDecl.version());
        assertEquals("UTF-8", xmlDecl.encoding());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p><a>Link</a></p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element pNode = doc.getElementsByTag("p").get(0);
        assertEquals(3, pNode.childSize());
        Element aNode = pNode.getElementsByTag("a").get(0);
        assertTrue(aNode.hasAttr("href"));
    }

    @Test
    public void testParseStartTag() {
        String html = "<p><a href=\"link.html\">Link</a></p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element pNode = doc.getElementsByTag("p").get(0);
        assertEquals(3, pNode.childSize());
        Element aNode = pNode.getElementsByTag("a").get(0);
        assertEquals("link.html", aNode.attr("href"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Document doc = Parser.parse(html, "http://test.com");
        Element imgNode = doc.getElementsByTag("img").get(0);
        assertEquals("image.png", imgNode.attr("src"));
        assertEquals("Image", imgNode.attr("alt"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a test</p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element pNode = doc.getElementsByTag("p").get(0);
        assertEquals("This is a test", pNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<p><![CDATA[This is some CDATA]]></p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element pNode = doc.getElementsByTag("p").get(0);
        assertEquals("This is some CDATA", pNode.getElementsByTag("cdata").get(0).text());
    }
}