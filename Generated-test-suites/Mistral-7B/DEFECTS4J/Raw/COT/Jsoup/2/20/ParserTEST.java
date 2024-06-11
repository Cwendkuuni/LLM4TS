package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test Paragraph</p>" +
                "<base href=\"/test/\">" +
                "<img src=\"image.jpg\" alt=\"Test Image\">" +
                "</body></html>";
        String baseUri = "http://example.com/test.html";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();
    }

    @Test
    public void testParse() {
        assertNotNull(parser.doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
        assertSame(doc.baseUri(), baseUri);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Body Fragment</p>" +
                "<script src=\"script.js\"></script>";
        Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(fragmentDoc.head());
        assertNotNull(fragmentDoc.body());
        assertEquals("Test Body Fragment", fragmentDoc.select("p").text());
        assertEquals(1, fragmentDoc.select("script").size());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment -->" +
                "<head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<!-- Test Comment 2 --></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Comment comment1 = doc.select("html > !comment()").get(0);
        Comment comment2 = doc.select("body > !comment()").get(0);
        assertEquals("Test Comment", comment1.text());
        assertEquals("Test Comment 2", comment2.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<xml version=\"1.0\" encoding=\"UTF-8\"></xml>";
        parser = new Parser(xmlDeclHtml, baseUri, false);
        doc = parser.parse();

        XmlDeclaration xmlDecl = doc.select("xml").get(0);
        assertEquals("UTF-8", xmlDecl.encoding());
        assertEquals("1.0", xmlDecl.version());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test Title</title>" +
                "<body><p>Test Paragraph<hr>" +
                "<h1>Test Heading</h1></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element hr = doc.select("body > p > hr").get(0);
        Element h1 = doc.select("body > h1").get(0);

        parser.parseEndTag();
        Element h1AfterParseEndTag = doc.select("body > h1").get(0);

        assertSame(hr.parent(), doc.body());
        assertSame(h1.parent(), doc.body());
        assertSame(h1AfterParseEndTag.parent(), doc.body());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseEndTagWithEmptyStack() {
        String html = "<html><body></body></html>";
        parser = new Parser(html, baseUri, false);
        parser.parseEndTag();
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test Title</title>" +
                "<body><p>Test Paragraph<img src=\"image.jpg\" alt=\"Test Image\"></p></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.select("body > p").get(0);
        Element img = p.select("img").get(0);

        parser.parseStartTag();
        Element newP = doc.select("body > p").get(1);

        assertSame(p.parent(), doc.body());
        assertSame(img.parent(), p);
        assertSame(newP.parent(), doc.body());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStartTagWithEmptyStack() {
        String html = "<html><body></body></html>";
        parser = new Parser(html, baseUri, false);
        parser.parseStartTag();
    }

    @Test
    public void testParseAttribute() {
        String html = "<p id=\"p1\" class=\"test class2\" title=\"Test Title\">Test Paragraph</p>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.select("p").get(0);
        Map<String, String> attributes = p.attributes();

        assertEquals("p1", attributes.get("id"));
        assertEquals("test class2", StringUtils.join(attributes.keySet(), " "));
        assertNull(attributes.get("nonExistent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAttributeWithInvalidKey() {
        String html = "<p id=\"p1\" class=\"test class2\" title=\"Test Title\">Test" +
                " Paragraph</p>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element p = doc.select("p").get(0);
        parser.parseAttribute();
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><head><title>Test Title</title>" +
                "<body><p>Test Paragraph</p></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        TextNode textNode = doc.select("p > text()").get(0);
        assertEquals("Test Paragraph", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><head><title>Test Title</title>" +
                "<body><script><![CDATA[function test() { alert('Test'); }]]></script></body></html>";
        parser = new Parser(html, baseUri, false);
        doc = parser.parse();

        Element script = doc.select("body > script").get(0);
        TextNode cdataNode = script.childNodes().get(0);
        assertEquals("function test() { alert('Test'); }", cdataNode.text());
    }
}