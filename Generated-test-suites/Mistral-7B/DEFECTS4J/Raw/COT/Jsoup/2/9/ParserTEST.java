package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParserWithValidHtmlAndBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element htmlNode = doc.elementSibling(0);
        Element headNode = htmlNode.child(0);
        Element titleNode = headNode.child(0);
        Element bodyNode = htmlNode.child(1);
        Element h1Node = bodyNode.child(0);

        assertEquals("html", htmlNode.tagName());
        assertEquals("head", headNode.tagName());
        assertEquals("title", titleNode.tagName());
        assertEquals("body", bodyNode.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParserBodyFragmentWithValidHtmlAndBaseUri() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);

        assertEquals("body", bodyNode.tagName());
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testParserWithNullHtml() {
        String html = null;
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    @Test(expected = NullPointerException.class)
    public void testParserWithNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = null;

        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        List<Node> commentNodes = doc.select("meta[property='og:comment']");
        assertEquals(0, commentNodes.size());

        Element htmlNode = doc.elementSibling(0);
        Comment commentNode = (Comment) htmlNode.child(0);

        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:og=\"http://ogp.me/ns#\">";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        XmlDeclaration xmlDecl = (XmlDeclaration) doc.selectSingleNode("//xml-mapping::xml-declaration()");
        assertEquals("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>", xmlDecl.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEndTagWithInvalidTagName() {
        String html = "<html><foo></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    @Test
    public void testParseStartTagWithEmptyTagName() {
        String html = "<html><body></body>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element bodyNode = doc.body();
        assertNull(bodyNode.child(0));
    }

    @Test
    public void testParseStartTagWithEmptyElement() {
        String html = "<html><body><img src=\"image.png\"/></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element imgNode = doc.selectSingleNode("//img");
        assertEquals("img", imgNode.tagName());
        assertNull(imgNode.childNodes());
    }

    @Test
    public void testParseStartTagWithDataTag() {
        String html = "<html><head><title>Test</title><script type=\"text/javascript\">function test() {alert('Hello');}</script></head><body><textarea>Hello World</textarea></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element titleNode = doc.selectSingleNode("//title");
        assertEquals("Test", titleNode.text());

        Element scriptNode = doc.selectSingleNode("//script");
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attr("type"));

        Element textareaNode = doc.selectSingleNode("//textarea");
        assertEquals("textarea", textareaNode.tagName());
        assertEquals("Hello World", textareaNode.child(0).text());
    }

    @Test
    public void testParseStartTagWithBaseTag() {
        String html = "<html><head/><body><base href=\"/new-path\"/></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("/new-path", doc.baseUri());
    }

    @Test
    public void testParseStartTagWithSelfClosedTag() {
        String html = "<html><body><img src=\"image.png\" />";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element imgNode = doc.selectSingleNode("//img");
        assertEquals("img", imgNode.tagName());
        assertNull(imgNode.childNodes());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><div class=\"red\" id=\"element1\" title=\"Tooltip\"></div></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element divNode = doc.selectSingleNode("//div[@class='red']");
        Map<String, String> attributes = divNode.attributes();
        assertEquals(3, attributes.size());
        assertEquals("red", attributes.get("class"));
        assertEquals("element1", attributes.get("id"));
        assertEquals("Tooltip", attributes.get("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element pNode = doc.selectSingleNode("//p");
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[var message = \"Hello World\";]]></script></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element scriptNode = doc.selectSingleNode("//script");
        TextNode textNode = (TextNode) scriptNode.child(0);
        assertEquals("var message = \"Hello World\";", textNode.text());
    }

    @Test
    public void testParseInvalidToken() {
        String html = "<html><body><script><![CDATA[var message = \"Hello World\";//]]></script></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        try {
            parser.parse();
            fail("Expected an IllegalArgumentException when parsing invalid token.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStackHasValidParent() {
        String html = "<html><body><h1></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertFalse(parser.stackHasValidParent(Tag.valueOf("h1")));
    }

    @Test
    public void testStackHasValidParentWithHtmlTag() {
        String html = "<html><head><title>Test</title></head><body><h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertTrue(parser.stackHasValidParent(Tag.valueOf("h1")));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<html><body><h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);

        Element containerNode = parser.popStackToSuitableContainer(Tag.valueOf("body"));
        assertEquals(bodyNode, containerNode);

        containerNode = parser.popStackToSuitableContainer(Tag.valueOf("html"));
        assertEquals(null, containerNode);
    }

    @Test
    public void testPopStackToClose() {
        String html = "<html><body><h1><h2></h2></h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element h1Node = doc.body().child(0);
        Element h2Node = h1Node.child(0);

        Element h2ToCloseNode = parser.popStackToClose(Tag.valueOf("h2"));
        assertEquals(h2Node, h2ToCloseNode);

        Element h1ToCloseNode = parser.popStackToClose(Tag.valueOf("h1"));
        assertEquals(h1Node, h1ToCloseNode);
    }
}