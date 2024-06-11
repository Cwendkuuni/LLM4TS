package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.numberOfElements());

        Element head = doc.head();
        assertEquals(headTag, head.tag());
        assertEquals(0, head.numberOfElements());

        Element body = doc.body();
        assertEquals(bodyTag, body.tag());
        assertEquals(1, body.numberOfElements());

        Element h1 = body.getFirstElementSibling();
        assertEquals(h1Tag, h1.tag());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.numberOfElements());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(1, doc.body().numberOfElements());
        assertEquals(pTag, doc.body().getFirstElementSibling().tag());
        assertEquals("Hello", doc.body().getFirstElementSibling().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.numberOfElements());
        assertNull(doc.head());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(1, doc.body().numberOfElements());
        assertEquals(pTag, doc.body().getFirstElementSibling());
        assertEquals("Hello", doc.body().getFirstElementSibling().text());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<h1><p>Hello</p></h1>", "http://example.com", false);
        parser.parseStartTag();
        assertSame(h1Tag, parser.last().tag());
        parser.parseStartTag();
        assertSame(pTag, parser.last().tag());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<h1><p>Hello</p></h1>", "http://example.com", false);
        parser.parseStartTag();
        parser.parseStartTag();
        parser.parseEndTag();
        assertSame(pTag, parser.last().tag());
        parser.parseEndTag();
        assertSame(h1Tag, parser.last().tag());
        parser.parseEndTag();
        assertNull(parser.last());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!DOCTYPE html><!-- Comments --><html><head></head><body></body></html>", "http://example.com", false);
        parser.parse();
        Comment comment = (Comment) parser.last();
        assertEquals("Comments", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", "http://example.com", false);
        XmlDeclaration xmlDecl = (XmlDeclaration) parser.last();
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", xmlDecl.systemId());
        assertEquals("1.0", xmlDecl.version());
        assertEquals(false, xmlDecl.isStandalone());
        parser.consume(); // consume >
        parser.parseStartTag();
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("<img src=\"image.jpg\" alt=\"Image\"></img>", "http://example.com", false);
        Attribute attr = parser.parseAttribute();
        assertEquals("src", attr.key());
        assertEquals("image.jpg", attr.value());
        parser.consume(); // consume >
        attr = parser.parseAttribute();
        assertEquals("alt", attr.key());
        assertEquals("Image", attr.value());
        parser.consume(); // consume >
        assertNull(parser.parseAttribute());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<p>Hello World</p>", "http://example.com", false);
        TextNode textNode = (TextNode) parser.last();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[Hello World]]>", "http://example.com", false);
        TextNode cdataNode = (TextNode) parser.last();
        assertEquals("Hello World", cdataNode.text());
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidHtml() {
        Parser parser = new Parser("</html>", "http://example.com", false);
        parser.parse();
    }

    @Test
    public void testParseSelfClosingTag() {
        Parser parser = new Parser("<img src=\"image.jpg\" alt=\"Image\"/>", "http://example.com", false);
        Element img = parser.last();
        assertEquals(imgTag, img.tag());
        assertEquals("image.jpg", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
    }

    @Test
    public void testParseEmptyElement() {
        Parser parser = new Parser("<br/>", "http://example.com", false);
        Element br = parser.last();
        assertEquals(brTag, br.tag());
    }
}