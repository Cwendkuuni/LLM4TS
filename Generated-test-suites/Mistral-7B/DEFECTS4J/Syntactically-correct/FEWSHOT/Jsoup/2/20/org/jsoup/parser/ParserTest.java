package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head());
        assertEquals(bodyTag, doc.body());
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.body());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World<script>alert('Hello!')</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head());
        assertEquals(bodyTag, doc.body());
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.select("h1").text());

        // Scripts should be ignored
        assertNull(doc.select("script").first());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.body());
        assertEquals(0, doc.body().childNodes().size());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(1, doc.select("comment()").size());
        assertEquals("This is a comment", doc.select("comment()").text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://foo.com\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");

        XmlDeclaration xmlDecl = doc.selectXml("xml-declaration()").first();
        assertEquals("xmlns:foo http://foo.com", xmlDecl.xmlns().get(0).toString());
        assertEquals("xml:lang en", xmlDecl.xmlLang().get(0).toString());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element head = doc.head();
        Element body = doc.body();

        Element title = head.select("title").first();
        Element h1 = body.select("h1").first();

        doc.remove(h1);

        assertEquals("1", String.valueOf(head.childNodes().size()));
        assertEquals("Test", title.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element head = doc.head();
        Element body = doc.body();
        Element title = head.select("title").first();
        Element h1 = body.select("h1").first();

        assertEquals("Test", title.text());
        assertEquals("Hello World", h1.text());

        Attributes h1Attributes = h1.attributes();
        assertEquals(0, h1Attributes.size());

        html = "<html><head><title>Test</title></head><body><h1 id=\"hello\" class=\"foo\">Hello World</h1></body></html>";
        doc = Parser.parse(html, "http://example.com");

        h1 = body.select("h1").first();
        assertEquals("hello", h1.id());
        assertEquals("foo", h1.classes().get(0));
        assertEquals(2, h1Attributes.size());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Image\" title=\"Caption\">";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.select("img").first();

        assertEquals("image.jpg", img.absUrl("src"));
        assertEquals("Image", img.attr("alt"));
        assertEquals("Caption", img.attr("title"));

        html = "<input type=\"text\" name=\"username\" value=\"John Doe\">";
        doc = Parser.parse(html, "http://example.com");

        Element input = doc.select("input[name=username]").first();

        assertEquals("John Doe", input.val());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.select("p").first();

        assertEquals("Hello World", p.text());

        html = "<html><body><p>Hello World<script>alert('Hello!')</script></p></body></html>";
        doc = Parser.parse(html, "http://example.com");

        p = doc.select("p").first();

        assertEquals("Hello World", p.textBefore("script"));
        assertEquals("", p.textAfter("script"));
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><p><![CDATA[Hello World]]></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element p = doc.select("p").first();

        TextNode cdata = (TextNode) p.child(0);
        assertEquals("Hello World", cdata.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/foo/\">";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("/foo/", doc.baseUri());
        assertEquals("http://example.com/foo/", doc.select("a").first().absUrl("href"));

        html = "<html><head><base href=\"/foo/\"><link rel=\"stylesheet\" href=\"/bar.css\"></head><body><a href=\"baz.html\">Link</a></body></html>";
        doc = Parser.parse(html, "http://example.com");

        assertEquals("/foo/bar.css", doc.select("link").first().absUrl("href"));
        assertEquals("/baz.html", doc.select("a").first().absUrl("href"));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.select("img").first();

        assertEquals(htmlTag, img.parent().tag());
    }

    @Test
    public void testParseInvalidParent() {
        String html = "<html><body><ul><li><img src=\"image.jpg\"></li></ul></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.select("img").first();

        assertNull(img.parent());
    }
}