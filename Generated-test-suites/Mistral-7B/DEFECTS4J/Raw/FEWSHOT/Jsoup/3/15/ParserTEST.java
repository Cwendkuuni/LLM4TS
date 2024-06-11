package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(4, doc.childNodeSize());

        Element htmlNode = doc.child(0);
        assertEquals(htmlTag, htmlNode.tagName());
        assertEquals(3, htmlNode.childNodeSize());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tagName());
        assertEquals(1, headNode.childNodeSize());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tagName());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(2, doc.childNodeSize());

        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseRelaxed() {
        String bodyHtml = "<div><p><b>Hello <i>World</i></b></p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element divNode = doc.body();
        assertEquals("div", divNode.tagName());
        assertEquals(1, divNode.childNodeSize());

        Element pNode = divNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals(1, pNode.childNodeSize());

        Element bNode = pNode.child(0);
        assertEquals("b", bNode.tagName());
        assertEquals("Hello ", bNode.text());

        Element iNode = bNode.child(0);
        assertEquals("i", iNode.tagName());
        assertEquals("World", iNode.text());

        Element nextSibling = iNode.nextSibling();
        assertNull(nextSibling);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser parser = new Parser(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
    }

    @Test
    public void testEmptyHtml() {
        String html = "";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testMalformedHtml() {
        String html = "<html><body>"; // missing head tag
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(0, bodyNode.childNodeSize());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        Comment comment = rootNode.child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is some CDATA]]></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        TextNode textNode = rootNode.child(0);
        assertEquals("This is some CDATA", textNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        XmlDeclaration xmlDeclaration = rootNode.child(0);
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlns());
        assertTrue(xmlDeclaration.isStandalone());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        Element bodyNode = rootNode.child(0);
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        parser.parseEndTag();
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, rootNode.childNodeSize());
        assertEquals(0, bodyNode.childNodeSize());

        assertFalse(h1Node.hasChildNodes());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        Element bodyNode = rootNode.child(0);
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        parser.parseStartTag();
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, rootNode.childSize());
        assertEquals(1, bodyNode.childNodeSize());
        assertEquals(1, bodyNode.childSize());

        Element newChild = new Element(Tag.valueOf("p"), null);
        parser.addChildToParent(newChild, false);

        assertEquals(2, bodyNode.childNodeSize());
        assertEquals(2, bodyNode.childSize());
        assertEquals("p", newChild.tagName());
        assertFalse(newChild.hasChildNodes());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Hello World</body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        Element bodyNode = rootNode.child(0);
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        TextNode textNode = bodyNode.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body class=\"test\" id=\"test\"><h1 title=\"Hello\">Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        Element bodyNode = rootNode.child(0);
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        List<Attribute> attributes = h1Node.attributes();
        assertEquals(2, attributes.size());

        Attribute classAttr = attributes.get(0);
        assertEquals("class", classAttr.key());
        assertEquals("test", classAttr.value());

        Attribute titleAttr = attributes.get(1);
        assertEquals("title", titleAttr.key());
        assertEquals("Hello", titleAttr.value());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><body><base href=\"/test\"><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodeSize());

        Element rootNode = doc.child(0);
        assertEquals(htmlTag, rootNode.tagName());
        assertEquals(1, rootNode.childNodeSize());

        Element bodyNode = rootNode.child(0);
        assertEquals(bodyTag, bodyNode.tagName());
        assertEquals(1, bodyNode.childNodeSize());

        Element baseNode = bodyNode.child(0);
        assertEquals("base", baseNode.tagName());
        assertEquals("/test", baseNode.absUrl("href"));

        Element h1Node = bodyNode.child(1);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        assertEquals("/test", doc.baseUri());
    }
}