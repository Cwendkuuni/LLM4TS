package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tag());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(headTag, doc.childNodes().get(0).childNodes().get(0).tag());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tag());
        assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(1).tag());
        assertEquals(1, doc.childNodes().get(1).childNodes().size());
        assertEquals(h1Tag, doc.childNodes().get(1).childNodes().get(0).tag());
        assertEquals("Hello World", doc.childNodes().get(1).childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());
        assertEquals(0, doc.head().childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tag());
        assertEquals("Hello World", doc.body().childNodes().get(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.body().childNodes().get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:my=\"http://example.com/ns\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDeclaration = doc.documentElement();
        assertEquals("http://example.com/ns", xmlDeclaration.xmlNsPrefixes().get("my"));
        assertEquals("en", xmlDeclaration.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p><b>Hello</b></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().childNodes().get(0);
        Element b = p.childNodes().get(0);
        assertEquals(pTag, p.tag());
        assertEquals(1, p.childNodes().size());
        assertEquals(bTag, b.tag());
        assertEquals("Hello", b.text());
        assertEquals(0, b.childNodes().size());
        assertEquals(pTag, b.parentTag());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><b>Hello</b></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().childNodes().get(0);
        assertEquals(pTag, p.tag());
        assertEquals(1, p.childNodes().size());
        Element b = p.childNodes().get(0);
        assertEquals(bTag, b.tag());
        assertEquals("Hello", b.text());
        assertEquals(0, b.childNodes().size());
        assertEquals(pTag, b.parentTag());

        // test with empty tag
        String htmlEmpty = "<html><body><p><img src=\"image.jpg\"/></p></body></html>";
        Document docEmpty = Parser.parse(htmlEmpty, "http://example.com");
        Element img = doc.body().childNodes().get(0);
        assertEquals(imgTag, img.tag());
        assertEquals(0, img.childNodes().size());
        assertEquals(pTag, img.parentTag());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><div id=\"foo\" class=\"bar\"></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element div = doc.body().childNodes().get(0);
        assertEquals("foo", div.id());
        assertEquals("bar", div.classes().get(0));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().childNodes().get(0);
        TextNode textNode = (TextNode) p.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        TextNode textNode = (TextNode) doc.body().childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><body><base href=\"/test\"/><a href=\"/test/page.html\">Page</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><body><script src=\"script.js\"></script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element script = doc.body().childNodes().get(0);
        assertEquals("script", script.tag().name());
        assertEquals("script.js", script.attr("src"));
        Element implicitParent = script.parent();
        assertEquals("body", implicitParent.tag().name());
    }

    @Test
    public void testParseImplicitParentWithHead() {
        String html = "<html><head><script src=\"script.js\"></script></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element head = doc.head();
        Element script = head.childNodes().get(0);
        assertEquals("script", script.tag().name());
        assertEquals("script.js", script.attr("src"));
        Element implicitParent = script.parent();
        assertEquals("head", implicitParent.tag().name());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body<p>Hello World</body></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseUnknownTag() {
        String html = "<html><body><unknown></body></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseNoClosingTag() {
        String html = "<html><body><input type=\"text\" name=\"username\"></body>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseNestedTags() {
        String html = "<html><body><p><b>Hello World</b></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element p = doc.body().childNodes().get(0);
        Element b = p.childNodes().get(0);
        assertEquals("p", p.tag().name());
        assertEquals("b", b.tag().name());
        assertEquals("Hello World", b.text());
        assertEquals(p, b.parentTag());
    }
}