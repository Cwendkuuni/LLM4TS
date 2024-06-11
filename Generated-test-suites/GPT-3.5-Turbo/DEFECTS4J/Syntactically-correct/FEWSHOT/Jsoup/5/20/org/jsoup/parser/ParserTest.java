package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Parser</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals("Test Parser", doc.title());
        assertEquals("Heading", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello, world!</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertTrue(doc.body().select("div").hasText());
        assertEquals("Hello, world!", doc.body().select("div").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!--This is a comment-->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Comment comment = doc.select("comment").first();
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        DataNode dataNode = doc.select("data").first();
        assertEquals("This is CDATA", dataNode.getWholeData());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        XmlDeclaration xmlDeclaration = doc.select("xml-declaration").first();
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Element p = doc.select("p").first();
        assertEquals("Hello, world!", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<p>Hello, <strong>world</strong>!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Element p = doc.select("p").first();
        Element strong = p.select("strong").first();
        assertEquals("Hello, ", p.text());
        assertEquals("world", strong.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        TextNode textNode = doc.select("text").first();
        assertEquals("Hello, world!", textNode.text());
    }

    @Test
    public void testPopStackToClose() {
        String html = "<html><body><p>Hello, world!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Element p = doc.select("p").first();
        assertNull(p);
    }
}