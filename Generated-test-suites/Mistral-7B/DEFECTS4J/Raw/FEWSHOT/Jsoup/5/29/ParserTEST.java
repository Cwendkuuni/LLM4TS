package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(htmlTag, last(doc).tagName());

        assertEquals(headTag, doc.head().tagName());
        assertNotNull(doc.title());
        assertEquals("Test", doc.title().text());

        assertEquals(bodyTag, doc.body().tagName());
        Element div = doc.body().child(0);
        assertEquals("div", div.tagName());
        Element p = div.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<div><p>Hello World</p></div>";
        Document doc = Parser.parseBodyFragment(html, "http://test.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(bodyTag, last(doc).tagName());

        Element div = doc.body().child(0);
        assertEquals("div", div.tagName());
        Element p = div.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<notag>Hello World</notag>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(bodyTag, last(doc).tagName());

        Element notag = doc.body().child(0);
        assertEquals("notag", notag.tagName());
        assertEquals("Hello World", notag.text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://test.com");

        assertEquals(htmlTag, doc.tagName());
        assertEquals(htmlTag, last(doc).tagName());
        assertEquals(0, doc.body().childCount());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body<div>";
        try {
            Document doc = Parser.parse(html, "http://test.com");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertEquals(0, Parser.getErrors().size());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element comment = doc.body().child(0);
        assertEquals("comment", comment.tagName());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ This is some CDATA ]]></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element cdata = doc.body().child(0);
        assertEquals("text", cdata.tagName());
        assertEquals("This is some CDATA", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Document doc = Parser.parse(html, "http://test.com");

        XmlDeclaration xmlDecl = doc.getXmlDecl();
        assertNotNull(xmlDecl);
        assertEquals("xmlns:foo=\"bar\"", xmlDecl.xmlns().toString());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element div = doc.body().child(0);
        Element p = div.child(0);

        doc.body().removeChild(div);

        Element p2 = doc.body().child(0);
        assertEquals("p", p2.tagName());
        assertEquals("Hello World", p2.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element div = doc.body().child(0);
        Element p = div.child(0);

        doc.body().insertBefore(p, new Element(bodyTag, "new p", new Attributes()));

        Element pNew = doc.body().child(1);
        assertEquals("p", pNew.tagName());
        assertEquals("", pNew.text());
        assertNull(pNew.attributes());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><div id=\"test\" class=\"test\"></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element div = doc.body().child(0);
        Attribute idAttr = div.attributes().get("id");
        Attribute classAttr = div.attributes().get("class");

        assertEquals("test", idAttr.value());
        assertEquals("test", classAttr.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><div>Hello World</div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element div = doc.body().child(0);
        TextNode textNode = (TextNode) div.child(0);

        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseIgnoreCase() {
        String html = "<html><body><div><p>Hello <b>World</b></p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element div = doc.body().child(0);
        Element p = div.child(0);
        Element b = p.child(0);

        assertEquals("b", b.tagName());
        assertEquals("World", b.text());
    }

    private static Element last(Document doc) {
        return doc.getElementsByTag("html").get(0).lastChild();
    }
}