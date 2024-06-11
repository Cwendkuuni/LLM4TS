package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.List;

public class ParserTest {

    private Parser createParser(String html, String baseUri) {
        return new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.select("h1").text());
        assertEquals("http://example.com/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Parser parser = createParser(bodyHtml, "http://example.com/");
        Document doc = parser.parse();
        assertEquals("Hello World", doc.select("h1").text());
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals(0, doc.head().childNodeSize());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><head><script><![CDATA[function test() { alert('Hello World'); }]]></script></head><body></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element script = doc.selectFirst("script");
        assertNotNull(script);
        TextNode cdata = (TextNode) script.child(0);
        assertEquals("[CDATA[function test() { alert('Hello World'); }]]", cdata.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Comment comment = doc.selectFirst("comment()[1]");
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><a href=\"https://example.com/\">Link</a></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element link = doc.selectFirst("a");
        assertEquals("Link", link.text());
        assertEquals("https://example.com/", link.absUrl("href"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><a href=\"https://example.com/\">Link</a></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element link = doc.selectFirst("a");
        assertEquals("Link", link.text());
        assertEquals("https://example.com/", link.absUrl("href"));
        Element body = doc.select("body").get(0);
        parser = createParser(html, "http://example.com/");
        Document doc2 = parser.parse();
        Element link2 = doc2.selectFirst("a");
        assertNull(link2);
        assertEquals(body, doc.body());
    }

    @Test
    public void testParseStartTagWithEmptyElement() {
        String html = "<img src=\"image.png\" />";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element img = doc.selectFirst("img");
        assertEquals("image.png", img.absUrl("src"));
        assertNull(img.child(0));
    }

    @Test
    public void testParseStartTagWithAttributes() {
        String html = "<input type=\"text\" name=\"username\" value=\"John\">";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element input = doc.selectFirst("input");
        assertEquals("text", input.tag().name());
        assertEquals("John", input.attr("value"));
        assertEquals("username", input.attr("name"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"https://example.com/\" title=\"Link Title\">Link</a>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element link = doc.selectFirst("a");
        assertEquals("Link Title", link.attr("title"));
        assertEquals("https://example.com/", link.absUrl("href"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        TextNode text = (TextNode) doc.select("p").get(0).child(0);
        assertEquals("Hello World", text.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHtml() {
        String html = "&lt;html&gt;<head&gt;<title>Test</title&gt;</head&gt;<body&gt;<h1&gt;Hello World&lt;/h1&gt;</body&gt;";
        Parser parser = createParser(html, "http://example.com/");
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        XmlDeclaration decl = doc.selectFirst("xml-declaration");
        assertEquals("http://www.w3.org/1999/xhtml", decl.namespaceUri());
        assertEquals("en", decl.language());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/foo\"><title>Test</title></head><body><a href=\"bar\"></a></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        assertEquals("/foo", doc.baseUri());
        assertEquals("bar", doc.selectFirst("a").absUrl("href"));
    }

    @Test
    public void testEmptyDocument() {
        Parser parser = createParser("", "http://example.com/");
        Document doc = parser.parse();
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><head></head><body><script src=\"script.js\"></script></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element script = doc.selectFirst("script");
        assertNotNull(script);
        Element head = script.parent();
        assertSame(doc.head(), head);
        assertEquals(1, head.childNodeSize());
    }

    @Test
    public void testParseInvalidTagName() {
        String html = "<html><body><abc></abc></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        Document doc = parser.parse();
        Element body = doc.select("body").get(0);
        assertSame(body, doc.body());
        assertEquals(0, body.childNodeSize());
    }

    @Test
    public void testParseMismatchedEndTag() {
        String html = "<html><body><a href=\"https://example.com/\"><div></a></div></body></html>";
        Parser parser = createParser(html, "http://example.com/");
        try {
            parser.parse();
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }
}