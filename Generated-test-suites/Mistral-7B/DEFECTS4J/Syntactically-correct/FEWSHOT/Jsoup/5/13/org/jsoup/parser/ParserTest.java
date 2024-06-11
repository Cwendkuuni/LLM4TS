package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        assertSame(htmlTag, htmlNode.tag());
        Element headNode = htmlNode.child(0);
        assertSame(headTag, headNode.tag());
        Element titleNode = headNode.child(0);
        assertSame(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());
        Element bodyNode = htmlNode.child(1);
        assertSame(bodyTag, bodyNode.tag());
        Element pNode = bodyNode.child(0);
        assertSame(Tag.valueOf("p"), pNode.tag());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element bodyNode = doc.body();
        Element pNode = bodyNode.child(0);
        assertSame(Tag.valueOf("p"), pNode.tag());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Element bodyNode = doc.body();
        Element pNode = bodyNode.child(0);
        assertSame(Tag.valueOf("p"), pNode.tag());
        assertEquals("Hello World", pNode.text());
        Element divNode = bodyNode.child(0);
        assertSame(Tag.valueOf("div"), divNode.tag());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals(htmlTag, doc.tag());
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<tag";
        try {
            Parser.parse(html, "http://test.com");
            fail("Expected ParserError");
        } catch (Parser.ParserError e) {
            // expected
        }
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        Element commentNode = htmlNode.child(0);
        assertSame(Comment.class, commentNode.getClass());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        XmlDeclaration xmlDeclNode = htmlNode.child(0);
        assertSame(XmlDeclaration.class, xmlDeclNode.getClass());
        assertEquals("xmlns:foo=\"bar\"", xmlDeclNode.xmlDecl());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        Element pNode = htmlNode.child(0);
        assertSame(Tag.valueOf("p"), pNode.tag());
        assertEquals("Hello World", pNode.text());
        Element bodyNode = htmlNode.parent();
        assertSame(bodyTag, bodyNode.tag());
        assertNull(bodyNode.previousSibling());
        assertNull(bodyNode.nextSibling());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        Element pNode = htmlNode.child(0);
        assertSame(Tag.valueOf("p"), pNode.tag());
        assertEquals("Hello World", pNode.text());
        Element titleNode = htmlNode.previousSibling();
        assertSame(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());
    }

    @Test
    public void testParseStartTagWithAttributes() {
        String html = "<p class=\"foo bar\" id=\"test\" data-baz=\"qux\">Hello World</p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element pNode = doc.body().child(0);
        assertSame(Tag.valueOf("p"), pNode.tag());
        assertEquals("Hello World", pNode.text());
        Map<String, Attribute> attributes = pNode.attributes();
        assertNotNull(attributes);
        Attribute classNameAttr = attributes.get("class");
        assertNotNull(classNameAttr);
        assertEquals("foo bar", classNameAttr.value());
        Attribute idAttr = attributes.get("id");
        assertNotNull(idAttr);
        assertEquals("test", idAttr.value());
        Attribute dataBazAttr = attributes.get("data-baz");
        assertNotNull(dataBazAttr);
        assertEquals("qux", dataBazAttr.value());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src=\"test.png\" />";
        Document doc = Parser.parse(html, "http://test.com");
        Element imgNode = doc.body().child(0);
        assertSame(Tag.valueOf("img"), imgNode.tag());
        assertTrue(imgNode.text().isEmpty());
        assertEquals("http://test.com/test.png", imgNode.absUrl("src"));
    }

    @Test(expected = Parser.ParserError.class)
    public void testParseInvalidStartTag() {
        String html = "<p<";
        Parser.parse(html, "http://test.com");
        fail("Expected ParserError");
    }

    @Test(expected = Parser.ParserError.class)
    public void testParseInvalidEndTag() {
        String html = "<html><p>Hello World</p></body></html>";
        try {
            Parser.parse(html, "http://test.com");
            fail("Expected ParserError");
        } catch (Parser.ParserError e) {
            // expected
        }
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "http://test.com");
        Element pNode = doc.body().child(0);
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[Hello World]]></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element htmlNode = doc.body();
        DataNode cdataNode = (DataNode) htmlNode.child(0);
        assertEquals("Hello World", cdataNode.text());
    }

    @Test(expected = Parser.ParserError.class)
    public void testParseInvalidCdata() {
        String html = "<html><![CDATA[Hello < World]]></html>";
        Parser.parse(html, "http://test.com");
        fail("Expected ParserError");
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/foo\"><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("http://test.com/foo", doc.baseUri());
        assertEquals("http://test.com/foo/Test.html", doc.absoluteUrl("/"));
    }
}