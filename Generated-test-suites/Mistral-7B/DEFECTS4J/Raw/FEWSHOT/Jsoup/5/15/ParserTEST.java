package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc.body();
        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", root.baseUri());

        Element head = root.child(0);
        assertEquals(headTag, head.tag());

        Element title = head.child(0);
        assertEquals(titleTag, title.tag());
        assertEquals("Test", title.text());

        Element body = root.child(1);
        assertEquals(bodyTag, body.tag());

        Element h1 = body.child(0);
        assertEquals(Tag.H1, h1.tag());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");
        Element root = doc;
        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", root.baseUri());
        assertEquals(bodyTag, root.child(0).tag());
        assertEquals("Hello World", root.child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><h1>Hello World</h1></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
        Element root = doc;
        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", root.baseUri());
        assertEquals(bodyTag, root.child(0).tag());
        Element div = root.child(0);
        assertEquals("div", div.tagName());
        assertEquals(1, div.childSize());
        assertEquals("Hello World", div.child(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Comment comment = doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<xml version='1.0'>";
        Document doc = Parser.parse(xmlDecl, "https://example.com");
        XmlDeclaration xmlDeclaration = doc.body();
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("https://example.com", xmlDeclaration.baseUri());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());

        Element body = doc.body();
        Element p2 = body.removeChild(0);
        assertNull(p2);
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());

        Element body = doc.body();
        body.appendChild(new Element(Tag.H1, "https://example.com").text("New H1"));
        assertEquals(2, body.childSize());
        assertEquals("p", body.child(0).tagName());
        assertEquals("Hello World", body.child(0).text());
        assertEquals("h1", body.child(1).tagName());
        assertEquals("New H1", body.child(1).text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<p id='foo' class='bar baz'></p>";
        Document doc = Parser.parse(html, "https://example.com");
        Element p = doc.body().child(0);
        assertEquals(1, p.attributeSize());

        Attribute idAttr = p.attr("id");
        assertEquals("foo", idAttr.value());

        Attribute classAttr = p.attr("class");
        assertEquals("bar baz", classAttr.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "https://example.com");
        TextNode textNode = doc.body().child(0).child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<p><![CDATA[Hello World]]></p>";
        Document doc = Parser.parse(html, "https://example.com");
        DataNode cdataNode = doc.body().child(0).child(0);
        assertEquals("Hello World", cdataNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/newdir'></head><body><a href='about.html'>About</a></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertEquals("/newdir", doc.baseUri());

        Element a = doc.body().child(0).child(0);
        assertEquals("https://example.com/newdir/about.html", a.absUrl("href"));
    }
}