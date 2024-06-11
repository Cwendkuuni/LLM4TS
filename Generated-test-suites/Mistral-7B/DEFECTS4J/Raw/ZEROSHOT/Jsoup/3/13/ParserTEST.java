package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        String html = "<html><head><title>Test Page</title></head>"
                + "<body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, "http://example.com");
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test Page", doc.title());
        assertTrue(doc.body().text().contains("Hello World"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertTrue(doc.body().text().contains("Hello World"));
        assertNull(doc.head());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        parser = new Parser(xmlDecl, "http://example.com");
        XmlDeclaration xmlDeclNode = (XmlDeclaration) parser.last();
        assertEquals("UTF-8", xmlDeclNode.encoding());
        assertEquals("1.0", xmlDeclNode.version());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --></html>";
        parser = new Parser(html, "http://example.com");
        Comment commentNode = (Comment) parser.last();
        assertEquals("Test Comment", commentNode.data());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        parser = new Parser(html, "http://example.com");
        parser.parse();
        assertEquals("p", parser.last().tagName());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, "http://example.com");
        Element h1Node = (Element) parser.last();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' alt='Test Image'>";
        parser = new Parser(html, "http://example.com");
        List<Attribute> attributes = parser.last().attributes();
        assertEquals(2, attributes.size());
        assertEquals("src", attributes.get(0).key());
        assertEquals("image.jpg", attributes.get(0).value());
        assertEquals("alt", attributes.get(1).key());
        assertEquals("Test Image", attributes.get(1).value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        parser = new Parser(html, "http://example.com");
        TextNode textNode = (TextNode) parser.last();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World');]]></script>";
        parser = new Parser(html, "http://example.com");
        DataNode cdataNode = (DataNode) parser.last();
        assertEquals("alert('Hello World');", cdataNode.data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html>"; // missing closing tag
        parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href='/subdir'><html><head><title>Test Page</title></head>"
                + "<body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, "http://example.com");
        assertEquals("/subdir", parser.doc().baseUri());
    }
}