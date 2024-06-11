package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParser_ValidHtml() {
        String html = "<html><head><title>Test</title></head><body><div><h1>Hello</h1></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        // Check document structure
        assertEquals(htmlTag, doc.tagName());
        Element htmlNode = doc.body();
        assertEquals(bodyTag, htmlNode.tagName());
        Element titleNode = doc.head().title();
        assertEquals(titleTag, titleNode.tagName());
        assertEquals("Test", titleNode.text());
        Element divNode = htmlNode.getElementsByTag("div").first();
        assertEquals("div", divNode.tagName());
        Element h1Node = divNode.getElementsByTag("h1").first();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());

        // Check base URI
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParser_EmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
    }

    @Test
    public void testParser_InvalidHtml() {
        String html = "<html<title";
        try {
            Document doc = Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParser_BodyFragment() {
        String html = "<div><h1>Hello</h1></div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element bodyNode = doc;
        assertEquals(bodyTag, bodyNode.tagName());
        Element h1Node = bodyNode.getElementsByTag("h1").first();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());
    }

    @Test
    public void testParser_RelaxedBodyFragment() {
        String html = "<div><h1>Hello</h1></div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com", true);
        Element bodyNode = doc;
        assertEquals(bodyTag, bodyNode.tagName());
        Element h1Node = bodyNode.getElementsByTag("h1").first();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());

        // No implicit tags are created
        Element divNode = bodyNode.getChild(0);
        assertNull(divNode.getElementsByTag("html").first());
        assertNull(divNode.getElementsByTag("head").first());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Hello World -->";
        Document doc = Parser.parse(html, "http://example.com");
        Element commentNode = doc.getElementsByTag("comment").first();
        assertEquals("comment", commentNode.tagName());
        assertEquals("Hello World", commentNode.text());
        assertEquals("http://example.com", commentNode.baseUri());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\">";
        Document doc = Parser.parse(html, "http://example.com");
        Element xmlDeclarationNode = doc.getElementsByTag("xml-declaration").first();
        assertEquals("xml-declaration", xmlDeclarationNode.tagName());
        assertEquals("1.0", xmlDeclarationNode.getAttribute("version"));
        assertEquals("UTF-8", xmlDeclarationNode.getAttribute("encoding"));
        assertEquals("yes", xmlDeclarationNode.getAttribute("standalone"));
        assertEquals("http://example.com", xmlDeclarationNode.baseUri());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello</p></div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element divNode = doc.getElementsByTag("div").first();
        Element pNode = divNode.getElementsByTag("p").first();
        assertEquals(2, divNode.childSize());
        assertEquals("p", pNode.tagName());
        assertEquals("Hello", pNode.text());
        Element emptyPNode = divNode.getElementsByTag("p").last();
        assertEquals(0, emptyPNode.childSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div><h1>Hello</h1></div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element divNode = doc.getElementsByTag("div").first();
        Element h1Node = divNode.getElementsByTag("h1").first();
        assertEquals(2, divNode.childSize());
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());

        html = "<input type=\"text\" name=\"name\" value=\"John\">";
        doc = Parser.parse(html, "http://example.com");
        Element inputNode = doc.getElementsByTag("input").first();
        assertEquals("input", inputNode.tagName());
        assertEquals("text", inputNode.getAttribute("type"));
        assertEquals("name", inputNode.getAttribute("name"));
        assertEquals("John", inputNode.getAttribute("value"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://example.com\" title=\"Test\">Link</a>";
        Document doc = Parser.parse(html, "http://example.com");
        Element linkNode = doc.getElementsByTag("a").first();
        Attribute hrefAttr = linkNode.attribute("href");
        Attribute titleAttr = linkNode.attribute("title");
        assertEquals("href", hrefAttr.key());
        assertEquals("http://example.com", hrefAttr.value());
        assertEquals("title", titleAttr.key());
        assertEquals("Test", titleAttr.value());

        html = "<img src=\"image.png\" alt=\"Image\" title=\"Caption\">";
        doc = Parser.parse(html, "http://example.com");
        Element imgNode = doc.getElementsByTag("img").first();
        Attribute srcAttr = imgNode.attribute("src");
        Attribute altAttr = imgNode.attribute("alt");
        Attribute titleAttr = imgNode.attribute("title");
        assertEquals("src", srcAttr.key());
        assertEquals("image.png", srcAttr.value());
        assertEquals("alt", altAttr.key());
        assertEquals("Image", altAttr.value());
        assertEquals("title", titleAttr.key());
        assertEquals("Caption", titleAttr.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "http://example.com");
        Element pNode = doc.getElementsByTag("p").first();
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Hello World", textNode.text());

        html = "<p>Hello<br/>World</p>";
        doc = Parser.parse(html, "http://example.com");
        pNode = doc.getElementsByTag("p").first();
        List<Node> children = Arrays.asList(pNode.child(0).getAllChildren());
        List<String> expectedTexts = Arrays.asList("Hello", "\n", "World");
        assertEquals(expectedTexts.size(), children.size());
        for (Node child : children) {
            if (child instanceof TextNode) {
                TextNode textNode1 = (TextNode) child;
                assertEquals(expectedTexts.get(Collections.indexOfList(expectedTexts, StringUtils.strip(textNode1.text()))), textNode1.text());
            }
        }
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[var x = 1; var y = 2;]]></script>";
        Document doc = Parser.parse(html, "http://example.com");
        Element scriptNode = doc.getElementsByTag("script").first();
        DataNode cdataNode = (DataNode) scriptNode.child(0);
        assertEquals("var x = 1; var y = 2;", cdataNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidXmlDecl() {
        String html = "<xml version=\"abc\">";
        Parser.parse(html, "http://example.com");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnmatchedTag() {
        String html = "<html><body><div>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_MissingEndTag() {
        String html = "<html><body><div<p>Hello</p>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_TagNestingError() {
        String html = "<html><body><div<div><div";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}