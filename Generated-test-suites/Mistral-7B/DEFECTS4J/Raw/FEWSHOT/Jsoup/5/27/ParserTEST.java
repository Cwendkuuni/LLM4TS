package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().text());
        assertEquals("http://example.com/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Hello World", doc.body().text());
        assertEquals("http://example.com/", doc.baseUri());
        assertNull(doc.head());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<![CDATA[ <html><body><script>alert('Hello World');</script></body> ]]>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Element scriptElement = doc.body().child(0);
        assertEquals("alert('Hello World');", scriptElement.data());
        assertEquals("http://example.com/", doc.baseUri());
        assertNull(doc.head());
    }

    @Test
    public void testParseStartTag() {
        parser.tq.consume("<html>");
        assertEquals(htmlTag, parser.last().tag());
    }

    @Test
    public void testParseEndTag() {
        parser.tq.consume("<html>");
        parser.tq.consume("<body>");
        parser.tq.consume("</body>");
        Element body = parser.last();
        Element html = parser.popStackToClose(bodyTag);
        assertSame(html, parser.last());
    }

    @Test
    public void testParseComment() {
        parser.tq.consume("<!-- This is a comment -->");
        Comment comment = (Comment) parser.last();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        parser.tq.consume("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
        XmlDeclaration xmlDeclaration = (XmlDeclaration) parser.last();
        assertNull(xmlDeclaration.encoding());
        assertNull(xmlDeclaration.standalone());
        assertNull(xmlDeclaration.version());
    }

    @Test
    public void testParseAttribute() {
        parser.tq.consume("<div id='foo' class='bar baz' data-foo='qux'>");
        Attribute idAttribute = parser.parseAttribute();
        assertEquals("id", idAttribute.key());
        assertEquals("foo", idAttribute.value());

        parser.tq.consumeWhitespace();
        Attribute classAttribute = parser.parseAttribute();
        assertEquals("class", classAttribute.key());
        String[] classValues = classAttribute.value().split("\\s+");
        assertEquals(3, classValues.length);
        assertEquals("bar", classValues[0]);
        assertEquals("baz", classValues[1]);
        assertEquals("qux", classValues[2]);
    }

    @Test
    public void testParseTextNode() {
        parser.tq.consume("<p>This is a text node</p>");
        TextNode textNode = (TextNode) parser.last();
        assertEquals("This is a text node", textNode.text());
    }

    @Test
    public void testParseCdata() {
        parser.tq.consume("<![CDATA[ This is a CDATA section ]]>");
        TextNode cdataNode = (TextNode) parser.last();
        assertEquals("This is a CDATA section", cdataNode.text());
    }

    @Test
    public void testParseEmptyElement() {
        parser.tq.consume("<img src='image.png' />");
        Element imgElement = parser.last();
        assertTrue(imgElement.isEmptyElement());
    }

    @Test
    public void testParseInvalidTag() {
        parser.tq.consume("<invalid>");
        assertNull(parser.last());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidEndTag() {
        parser.tq.consume("<html><body></html>");
    }

    @Test
    public void testParseDataTag() {
        parser.tq.consume("<script type='text/javascript'>console.log('Hello World');</script>");
        Element scriptElement = parser.last();
        assertEquals("script", scriptElement.tagName());
        DataNode scriptData = (DataNode) scriptElement.child(0);
        assertEquals("console.log('Hello World');", scriptData.text());
    }

    @Test
    public void testParseBaseTag() {
        String baseHtml = "<html><head><base href='/test'><title>Test</title></head><body><a href='/about'>About</a></body></html>";
        parser = new Parser(baseHtml, baseUri, false);
        parser.parse();
        assertEquals("/test", doc.baseUri());
        assertEquals("/about", doc.select("a").get(0).absUrl("href"));
    }
}