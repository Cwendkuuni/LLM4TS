package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals(html, doc.html());

        Element htmlNode = doc.elementSiblings().get(0);
        assertEquals("html", htmlNode.tagName());
        assertEquals("http://example.com", htmlNode.baseUri());

        Element headNode = htmlNode.childElements().get(0);
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.childElements().get(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.childElements().get(1);
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.childElements().get(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<body><h1>Hello World</h1></body>", doc.html());

        Element htmlNode = doc.elementSiblings().get(0);
        assertEquals("html", htmlNode.tagName());
        assertEquals("http://example.com", htmlNode.baseUri());

        Element bodyNode = htmlNode.childElements().get(0);
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.childElements().get(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "http://example.com");
        parser.parse();
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser parser = new Parser("", "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTag() {
        String html = "<html><head><title>Test</title></head><body><foo></foo></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<html><!-- This is a comment --></html>", doc.html());

        List<Node> commentNodes = doc.body().childNodes();
        assertEquals(1, commentNodes.size());
        assertEquals(Node.COMMENT_NODE, commentNodes.get(0).nodeType());
        assertEquals("This is a comment", commentNodes.get(0).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">", doc.html());

        Element htmlNode = doc.elementSiblings().get(0);
        XmlDeclaration xmlDecl = htmlNode.child(0);
        assertEquals("http://example.com/foo", xmlDecl.namespaceUri());
        assertEquals("en", xmlDecl.language());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.html());

        Element htmlNode = doc.elementSiblings().get(0);
        Element bodyNode = htmlNode.childElements().get(0);
        assertEquals("body", bodyNode.tagName());
        assertEquals(1, bodyNode.childNodes().size());
        assertEquals(Node.ELEMENT_NODE, bodyNode.childNodes().get(0).nodeType());
        assertEquals("p", ((Element) bodyNode.childNodes().get(0)).tagName());
        assertEquals("Hello World", ((Element) bodyNode.childNodes().get(0)).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"title\">Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<html><body><h1 id=\"title\">Hello World</h1></body></html>", doc.html());

        Element htmlNode = doc.elementSiblings().get(0);
        Element bodyNode = htmlNode.childElements().get(0);
        Element h1Node = bodyNode.childElements().get(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
        assertEquals("title", h1Node.getAttributeByName("id"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<input type=\"text\" name=\"username\" value=\"test\">";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element inputNode = (Element) doc.select("input").get(0);
        assertEquals("input", inputNode.tagName());
        assertEquals("text", inputNode.attributes().get("type").getValue());
        assertEquals("username", inputNode.attributes().get("name").getValue());
        assertEquals("test", inputNode.attributes().get("value").getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.html());

        Element pNode = (Element) doc.select("p").get(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[function test(){ alert('Hello World'); }]]></script></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("<html><body><script><![CDATA[function test(){ alert('Hello World'); }]]></script></body></html>", doc.html());

        Element scriptNode = (Element) doc.select("script").get(0);
        assertEquals("script", scriptNode.tagName());
        assertTrue(scriptNode.data().startsWith("function test("));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag() {
        String html = "<html><body><x></x></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidEndTag() {
        String html = "<html><body><div></div></foo>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTagName() {
        String html = "<html><body><foo></foo></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }
}