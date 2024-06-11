package org.jsoup.parser;

import static org.junit.Assert.*;
import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Test cases for the Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element htmlNode = doc.elementSibling(0);
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Fragment</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element htmlNode = doc.elementSibling(0);
        assertEquals(htmlTag, htmlNode.tag());

        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Fragment", h1Node.text());
    }

    @Test
    public void testParseRelaxedBodyFragment() {
        String bodyHtml = "<body><div><h1>Fragment</h1></div>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element htmlNode = doc.elementSibling(0);
        assertEquals(htmlTag, htmlNode.tag());

        Element bodyNode = htmlNode.child(0);
        assertEquals(bodyTag, bodyNode.tag());

        Element divNode = bodyNode.child(0);
        assertEquals("div", divNode.tagName());
        Element h1Node = divNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Fragment", h1Node.text());
    }

    @Test
    public void testEmptyParser() {
        String html = "";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals("", doc.outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParser() {
        String html = null;
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriParser() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null);
        parser.parse();
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\" />";
        Parser parser = new Parser(html, "http://example.com/images");
        Document doc = parser.parse();

        Element imgNode = doc.child(0);
        assertEquals("img", imgNode.tagName());
        String imgSrc = imgNode.attr("src");
        assertEquals("http://example.com/images/image.png", imgSrc);
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World');]]></script>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element scriptNode = doc.child(0);

        assertEquals("script", scriptNode.tagName());
        String scriptText = scriptNode.text();
        assertEquals("alert('Hello World');", scriptText);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element commentNode = doc.child(0);

        assertEquals("-->", commentNode.outerHtml());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element xmlDeclNode = doc.child(0);

        assertEquals("xml-decl", xmlDeclNode.tagName());
        assertEquals("http://example.com", xmlDeclNode.attr("baseURI"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element pNode = doc.child(0);
        TextNode textNode = (TextNode) pNode.child(0);

        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.png\" alt=\"Image\" title=\"Title\" />";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element imgNode = doc.child(0);

        assertEquals("src", imgNode.attrName(0));
        assertEquals("image.png", imgNode.attr("src"));

        assertEquals("alt", imgNode.attrName(1));
        assertEquals("Image", imgNode.attr("alt"));

        assertEquals("title", imgNode.attrName(2));
        assertEquals("Title", imgNode.attr("title"));
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<title>Test</title>";
        try {
            Parser parser = new Parser(html, "http://example.com");
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<img src=\"image.png\" alt=\"Image\" />";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element imgNode = doc.child(0);
        assertEquals("img", imgNode.tagName());
        Element divNode = new Element(Tag.valueOf("div"), "http://example.com");
        imgNode.appendChild(divNode);
        assertEquals(1, divNode.childrenSize());
        assertEquals("img", divNode.child(0).tagName());
    }

    @Test
    public void testParseInvalidTagSequence() {
        String html = "<html><head><title>Test</title></title></head>";
        try {
            Parser parser = new Parser(html, "http://example.com");
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidTagSequenceWithImplicitParents() {
        String html = "<img src=\"image.png\" alt=\"Image\" />";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element imgNode = doc.child(0);
        assertEquals("img", imgNode.tagName());

        Element divNode = new Element(Tag.valueOf("div"), "http://example.com");
        imgNode.appendChild(divNode);
        assertEquals(1, divNode.childrenSize());
        assertEquals("img", divNode.child(0).tagName());

        Element pNode = new Element(Tag.valueOf("p"), "http://example.com");
        divNode.appendChild(pNode);
        assertEquals(1, pNode.childrenSize());
        assertEquals("p", pNode.child(0).tagName());

        Element h1Node = new Element(Tag.valueOf("h1"), "http://example.com");
        pNode.appendChild(h1Node);
        assertEquals(1, h1Node.childrenSize());
        assertEquals("h1", h1Node.child(0).tagName());

        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidTagSequenceWithSelfClosingTag() {
        String html = "<img src=\"image.png\" alt=\"Image\" />";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element imgNode = doc.child(0);
        assertEquals("img", imgNode.tagName());

        Element divNode = new Element(Tag.valueOf("div"), "http://example.com");
        imgNode.appendChild(divNode);
        assertEquals(1, divNode.childrenSize());
        assertEquals("img", divNode.child(0).tagName());

        Element pNode = new Element(Tag.valueOf("p"), "http://example.com");
        divNode.appendChild(pNode);
        assertEquals(1, pNode.childrenSize());
        assertEquals("p", pNode.child(0).tagName());

        Element imgNode2 = new Element(Tag.valueOf("img"), "http://example.com");
        imgNode.appendChild(imgNode2);
        assertEquals(1, imgNode2.childrenSize());
        assertEquals("img", imgNode2.tagName());
        assertEquals(true, imgNode2.isSelfClosing());

        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlDeclWithDoctype() {
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World</p>\n" +
                "</body>\n" +
                "</html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element xmlDeclNode = doc.child(0);
        assertEquals("xml-decl", xmlDeclNode.tagName());

        Element htmlNode = doc.child(1);
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.child(0);
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals("body", bodyNode.tagName());

        Element pNode = bodyNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());

        assertNotNull(doc.xmlRootElement());
    }
}