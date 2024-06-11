package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>The Dormouse's story</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"title\"><b>The Dormouse's story</b></p>\n" +
                "<p class=\"story\">Once upon a time there were three little sisters;</p>\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        assertEquals("The Dormouse's story", doc.title());
        assertEquals("html", doc.tagName());
        assertFalse(doc.head() == null);
        assertFalse(doc.body() == null);

        Element p = doc.select("p.title").first();
        assertEquals("p", p.tagName());
        assertEquals("title", p.parent().tagName());
        assertEquals("The Dormouse's story", p.text());

        p = doc.select("p.story").first();
        assertEquals("p", p.tagName());
        assertEquals("body", p.parent().tagName());
        assertEquals("Once upon a time there were three little sisters;\n", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p class=\"title\"><b>The Dormouse's story</b></p>\n" +
                "<p class=\"story\">Once upon a time there were three little sisters;</p>";

        Parser parser = new Parser(bodyHtml, null, true);
        Document doc = parser.parse();
        assertEquals("The Dormouse's story", doc.title());
        assertNull(doc.head());
        assertFalse(doc.body() == null);

        Element p = doc.select("p.title").first();
        assertEquals("p", p.tagName());
        assertEquals("body", p.parent().tagName());
        assertEquals("The Dormouse's story", p.text());

        p = doc.select("p.story").first();
        assertEquals("p", p.tagName());
        assertEquals("body", p.parent().tagName());
        assertEquals("Once upon a time there were three little sisters;\n", p.text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><p>Hello<script>alert('hi');</script>World";
        Parser parser = new Parser(html, null, false);
        try {
            parser.parse();
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Encountered <script> at line 2, column 11"));
        }
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertFalse(doc.head() == null);
        assertFalse(doc.body() == null);
        assertTrue(doc.isEmpty());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>The Dormouse's story</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"title\"><b>The Dormouse's story</b></p>\n" +
                "<p class=\"story\">Once upon a time there were three little sisters;</p>\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        XmlDeclaration decl = doc.selectXml("xml").first();
        assertEquals("version", decl.attribute("version"));
        assertEquals("UTF-8", decl.attribute("encoding"));
        assertFalse(decl.isStandalone());
    }

    @Test
    public void testParseComments() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<!--\n" +
                "This is a comment\n" +
                "-->\n" +
                "<head>\n" +
                "<title>The Dormouse's story</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"title\"><b>The Dormouse's story</b></p>\n" +
                "<p class=\"story\">Once upon a time there were three little sisters;</p>\n" +
                "<!--\n" +
                "This is another comment\n" +
                "-->\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Comment comment1 = doc.select("!--This is a comment--").first();
        assertEquals("This is a comment", comment1.text());
        Comment comment2 = doc.select("!--This is another comment--").first();
        assertEquals("This is another comment", comment2.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>The Dormouse's story</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"title\"><b>The Dormouse's story</b></p>\n" +
                "<p class=\"story\">Once upon a time <![CDATA[There was an apple tree]]> there were three little sisters;</p>\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        TextNode textNode = doc.select("p.story text()").first().child(1);
        assertEquals("There was an apple tree", textNode.text());
    }

    @Test
    public void testParseAttributes() {
        String html = "<p class=\"title\" id=\"p1\">The Dormouse's story</p>";

        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        Element p = doc.select("p").first();
        Map<String, String> attributes = p.attributes();
        assertEquals(2, attributes.size());
        assertEquals("title", attributes.get("class"));
        assertEquals("p1", attributes.get("id"));
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href=\"/foo/\">\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>The Dormouse's story</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"title\"><b>The Dormouse's story</b></p>\n" +
                "<p class=\"story\">Once upon a time there were three little sisters;</p>\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();
        assertEquals("/foo/", doc.baseUri());
    }
}