package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        assertEquals("Test Page", doc.title());
        assertEquals("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>", doc.html());
        assertEquals("Hello World!", doc.select("h1").text());

        Element htmlNode = doc.select("html").first();
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.select("title").first();
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Page", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.select("h1").first();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World!", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id='content'>Hello World!</div>";
        Parser parser = new Parser(bodyHtml, "http://example.com/", true);
        Document doc = parser.parse();

        assertEquals("<html><head><title>Anonymous Page</title></head><body id='content'>Hello World!</body></html>", doc.html());
        assertEquals("Hello World!", doc.select("#content").text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<p>Hello World!<a href='link.html'>Link</a></p>";
        Parser parser = new Parser(bodyHtml, "http://example.com/", true);
        Document doc = parser.parse();

        assertEquals("<html><head><title>Anonymous Page</title></head><body><p>Hello World!<a href='link.html'>Link</a></p></body></html>", doc.html());
        assertEquals("Hello World!", doc.select("p").text());
        assertEquals("link.html", doc.select("a").attr("href"));
    }

    @Test
    public void testParseEmptyDocWithRelaxedParsing() {
        String html = "<html></html>";
        Parser parser = new Parser(html, "http://example.com/", true);
        Document doc = parser.parse();

        assertEquals("<html><head><title>Untitled Document</title></head><body></body></html>", doc.html());
    }

    @Test
    public void testParseInvalidInput() {
        Parser parser = new Parser("Invalid input", "http://example.com/");
        try {
            parser.parse();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version='1.0' encoding='UTF-8'?>";
        Parser parser = new Parser(xmlDecl, "http://example.com/");

        Document doc = parser.parse();
        XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.select("XmlDeclaration").first();
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("1.0", xmlDeclaration.version());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Comment comment = (Comment) doc.select("comment()").first();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ <p>This is a CDATA section</p> ]]>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        TextNode textNode = (TextNode) doc.select("p").first();
        assertEquals("This is a CDATA section", textNode.text());
    }

    @Test
    public void testParseTagWithAttributes() {
        String html = "<img src='image.jpg' title='Test Image' alt='Test Image'>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element imgNode = doc.select("img").first();
        assertEquals("img", imgNode.tagName());
        assertEquals("image.jpg", imgNode.attr("src"));
        assertEquals("Test Image", imgNode.attr("title"));
        assertEquals("Test Image", imgNode.attr("alt"));
    }

    @Test
    public void testParseTagWithEmptyAttribute() {
        String html = "<input type='text' name='username' value=''>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element inputNode = doc.select("input").first();
        assertEquals("input", inputNode.tagName());
        assertEquals("text", inputNode.attr("type"));
        assertEquals("username", inputNode.attr("name"));
        assertNull(inputNode.attr("value"));
    }

    @Test
    public void testParseTagWithImplicitParent() {
        String html = "<base href='sub.html'><script src='script.js'></script>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element baseNode = doc.select("base").first();
        assertEquals("base", baseNode.tagName());
        assertEquals("sub.html", baseNode.attr("href"));
        assertEquals("http://example.com/sub.html", baseNode.absUrl("href"));

        Element scriptNode = doc.select("script").first();
        assertEquals("script", scriptNode.tagName());
        assertEquals("script.js", scriptNode.attr("src"));
        assertEquals("http://example.com/script.js", scriptNode.absUrl("src"));
    }

    @Test
    public void testParseTagWithImplicitParentInBody() {
        String html = "<base href='sub.html'><body><script src='script.js'></script></body>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element baseNode = doc.select("base").first();
        assertEquals("base", baseNode.tagName());
        assertEquals("sub.html", baseNode.attr("href"));
        assertEquals("http://example.com/sub.html", baseNode.absUrl("href"));

        Element scriptNode = doc.select("script").first();
        assertEquals("script", scriptNode.tagName());
        assertEquals("script.js", scriptNode.attr("src"));
        assertEquals("http://example.com/script.js", scriptNode.absUrl("src"));

        Element bodyNode = doc.select("body").first();
        assertEquals("body", bodyNode.tagName());
        assertEquals(2, bodyNode.childSize());
    }

    @Test
    public void testParseTagWithImplicitParentInHead() {
        String html = "<head><base href='sub.html'><title>Page Title</title></head><body><script src='script.js'></script></body>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element baseNode = doc.select("base").first();
        assertEquals("base", baseNode.tagName());
        assertEquals("sub.html", baseNode.attr("href"));
        assertEquals("http://example.com/sub.html", baseNode.absUrl("href"));

        Element titleNode = doc.select("title").first();
        assertEquals("title", titleNode.tagName());
        assertEquals("Page Title", titleNode.text());

        Element scriptNode = doc.select("script").first();
        assertEquals("script", scriptNode.tagName());
        assertEquals("script.js", scriptNode.attr("src"));
        assertEquals("http://example.com/script.js", scriptNode.absUrl("src"));

        Element headNode = doc.select("head").first();
        assertEquals("head", headNode.tagName());
        assertEquals(3, headNode.childSize());
    }

    @Test
    public void testParseTagWithMixedImplicitAndExplicitParents() {
        String html = "<html><head><title>Page Title</title><base href='sub.html'><body><script src='script.js'></script></body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element htmlNode = doc.select("html").first();
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.select("title").first();
        assertEquals("title", titleNode.tagName());
        assertEquals("Page Title", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());

        Element scriptNode = bodyNode.select("script").first();
        assertEquals("script", scriptNode.tagName());
        assertEquals("script.js", scriptNode.attr("src"));
        assertEquals("http://example.com/script.js", scriptNode.absUrl("src"));

        Element baseNode = doc.select("base").first();
        assertEquals("base", baseNode.tagName());
        assertEquals("sub.html", baseNode.attr("href"));
        assertEquals("http://example.com/sub.html", baseNode.absUrl("href"));
    }
}