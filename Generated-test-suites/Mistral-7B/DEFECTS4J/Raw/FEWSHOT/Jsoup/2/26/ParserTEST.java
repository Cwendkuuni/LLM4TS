package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.AttributeSelector;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals("Hello World", doc.body().select("h1").text());
        assertNull(doc.head());
    }

    @Test
    public void testParseEmpty() {
        String html = "";
        Document doc = Parser.parse(html, "https://example.com");
        assertNull(doc);
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>";
        Document doc = Parser.parse(html, "https://example.com");

        XmlDeclaration decl = (XmlDeclaration) doc.selectFirst("xml[1]");
        assertEquals("1.0", decl.version());
        assertEquals("UTF-8", decl.encoding());
        assertFalse(decl.standalone());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "https://example.com");
        Element comment = doc.selectFirst("#comment");
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello</p></div>";
        Document doc = Parser.parse(html, "https://example.com");
        Element div = doc.selectFirst("#div");
        Element p = div.selectFirst("#p");
        assertEquals(1, div.childNodeSize());
        assertEquals(1, p.childNodeSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div id='div' class='class'><p id='p'>Hello <img src='image.png' alt='Image'/></p></div>";
        Document doc = Parser.parse(html, "https://example.com");
        Element div = doc.selectFirst("#div");
        assertEquals("div", div.tagName());
        assertEquals(2, div.childNodeSize());
        Element p = div.selectFirst("#p");
        assertEquals("p", p.tagName());
        assertEquals(3, p.childNodeSize());
        Element img = p.selectFirst("#img");
        assertEquals("img", img.tagName());
        assertEquals("image.png", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
        assertEquals(2, img.attributeCount());
        Attribute classAttr = p.attr("class");
        assertEquals("class", classAttr.getName());
        assertEquals("class", classAttr.toString());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id='div' class='class' style='color:red'><p class='class'></p></div>";
        Document doc = Parser.parse(html, "https://example.com");
        Element div = doc.selectFirst("#div");
        Attribute idAttr = div.attr("id");
        assertEquals("id", idAttr.getName());
        assertEquals("div", idAttr.toString());
        Attribute classAttr = div.attr("class");
        assertEquals("class", classAttr.getName());
        assertEquals("class", classAttr.toString());
        Attribute styleAttr = div.attr("style");
        assertEquals("style", styleAttr.getName());
        assertEquals("color:red", styleAttr.toString());
    }

    @Test
    public void testParseTextNode() {
        String html = "<div>Hello World</div>";
        Document doc = Parser.parse(html, "https://example.com");
        Element div = doc.selectFirst("#div");
        TextNode textNode = (TextNode) div.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[var x = 1;]]></script>";
        Document doc = Parser.parse(html, "https://example.com");
        Element script = doc.selectFirst("#script");
        DataNode cdataNode = (DataNode) script.child(0);
        assertEquals("var x = 1;\n", cdataNode.text());
    }

    @Test
    public void testParseBase() {
        String html = "<base href='/subdir/'><html><head><title>Test</title></head><body><a href='link.html'>Link</a></body></html>";
        Document doc = Parser.parse(html, "https://example.com/");
        assertEquals("/subdir/link.html", doc.location().absUrl("link.html"));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<img src='image.png' alt='Image' />";
        Document doc = Parser.parse(html, "https://example.com");
        Element img = doc.selectFirst("#img");
        Element parent = img.parent();
        assertEquals("body", parent.tagName());
    }

    @Test
    public void testParseInvalidParent() {
        String html = "<html><body><h1>Hello</h1><h2>World</h2></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element h1 = doc.selectFirst("#h1");
        Element h2 = doc.selectFirst("#h2");
        assertEquals(1, h1.childNodeSize());
        assertEquals(0, h2.childNodeSize());
        assertNull(h2.parent());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src='image.png' alt='Image' />";
        Document doc = Parser.parse(html, "https://example.com");
        Element img = doc.selectFirst("#img");
        assertEquals(0, img.childNodeSize());
    }
}