package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        Element title = doc.head().title();
        assertEquals("Test", title.text());
        Element h1 = doc.body().getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");
        assertEquals(htmlTag, doc.tagName());
        Element h1 = doc.body().getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());
        assertEquals(0, doc.head().childElementCount());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element comment = doc.body().child(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "https://example.com");
        XmlDeclaration decl = doc.head().getElementsByTag("xml-decl").first();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", decl.xmlns("xsi"));
        assertEquals("en", decl.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element title = doc.head().title();
        Element h1 = doc.body().getElementsByTag("h1").first();
        doc.body().removeChild(h1);
        assertNull(doc.body().getElementsByTag("h1").first());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1 id=\"h1\">Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element title = doc.head().title();
        Element h1 = doc.body().getElementsByTag("h1").first();
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
        assertEquals("h1", h1.id());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Image\" title=\"Title\"/>";
        Document doc = Parser.parse(html, "https://example.com");
        Element img = doc.body().getElementsByTag("img").first();
        assertEquals("image.jpg", img.absUrl("src"));
        assertEquals("Image", img.attr("alt"));
        assertEquals("Title", img.attr("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element p = doc.body().getElementsByTag("p").first();
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[function test(){alert('Hello World');}]]></script></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element script = doc.body().getElementsByTag("script").first();
        assertEquals(1, script.childElementCount());
        Element cdata = script.child(0);
        assertTrue(cdata instanceof DataNode);
        assertEquals("function test(){alert('Hello World');}", cdata.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/test/\"><title>Test</title></head><body><a href=\"foo.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals("/test/foo.html", doc.baseUri());
        assertEquals("https://example.com/test/foo.html", doc.head().title().absUrl(""));
    }
}