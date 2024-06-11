package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Elements.html;
import static org.jsoup.nodes.Elements.text;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body>" +
                "<p id=\"p1\">Hello <em>world!</em></p>" +
                "<p id=\"p2\"><base href=\"/test/\">" +
                "<img src=\"image.jpg\" alt=\"Image\" />" +
                "<script type=\"text/javascript\">function test() { alert('Test'); }</script>" +
                "</body></html>";
        parser = new Parser(html, null, false);
    }

    @Test
    public void testParse() {
        Document document = parser.parse();
        assertEquals(htmlTag, document.tagName());
        assertEquals(8, document.childNodeSize());

        Element htmlElement = document.getElementById("p1");
        assertEquals("p", htmlElement.tagName());
        assertEquals("Hello <em>world!</em>", htmlElement.text());
        assertEquals("p1", htmlElement.id());

        Element headElement = document.getElementsByTag("head").first();
        assertEquals(1, headElement.childNodeSize());
        Element titleElement = headElement.getElementsByTag("title").first();
        assertEquals("title", titleElement.tagName());
        assertEquals("Test", titleElement.text());

        Element baseElement = document.getElementsByTag("base").first();
        assertEquals("base", baseElement.tagName());
        assertEquals("/test/", baseElement.attr("href"));

        Element imgElement = document.getElementsByTag("img").first();
        assertEquals("img", imgElement.tagName());
        assertEquals("Image", imgElement.attr("alt"));

        Element scriptElement = document.getElementsByTag("script").first();
        assertEquals("script", scriptElement.tagName());
        assertEquals("text/javascript", scriptElement.attr("type"));
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Hello world!</p>", null);
        assertEquals(htmlTag, document.tagName());
        assertEquals(1, document.childNodeSize());
        assertEquals(1, document.getElementsByTag("body").size());
        Element bodyElement = document.getElementsByTag("body").first();
        assertEquals(1, bodyElement.childNodeSize());
        assertEquals("p", bodyElement.get(0).tagName());
        assertEquals("Hello world!", bodyElement.get(0).text());
    }

    @Test
    public void testParseEmptyHtml() {
        Document document = Parser.parse("<html></html>", null);
        assertEquals(htmlTag, document.tagName());
        assertEquals(0, document.childNodeSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        new Parser(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        new Parser("<html></html>", null, false);
    }

    @Test
    public void testParseWithBaseUri() {
        String baseUri = "https://example.com/test";
        Document document = Parser.parse("<img src=\"image.jpg\" alt=\"Image\" />", baseUri);
        Element imgElement = document.getElementsByTag("img").first();
        assertEquals("img", imgElement.tagName());
        assertEquals("image.jpg", imgElement.absUrl("src"));
    }

    @Test
    public void testParseWithBaseFragmentUri() {
        String baseUri = "#fragment";
        Document document = Parser.parse("<a href=\"#fragment\">Link</a>", baseUri);
        Element linkElement = document.getElementsByTag("a").first();
        assertEquals("a", linkElement.tagName());
        assertEquals("#fragment", linkElement.absUrl("href"));
    }

    @Test
    public void testParseXmlDeclaration() {
        String xmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>";
        Document document = new Parser(xmlDeclaration, null, false).parse();
        XmlDeclaration xml = (XmlDeclaration) document.getElementsByTag("xml-decl").get(0);
        assertEquals("UTF-8", xml.encoding());
        assertEquals("1.0", xml.version());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment --><p>Hello world!</p>";
        Document document = Parser.parse(html, null);
        Element commentElement = document.getElementsByTag("comment").first();
        assertEquals("comment", commentElement.tagName());
        assertEquals("This is a comment", commentElement.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<script>function test() { alert('Test'); }</script>]]>";
        Document document = Parser.parse(html, null);
        Element scriptElement = document.getElementsByTag("script").first();
        assertEquals("script", scriptElement.tagName());
        assertEquals("<script>function test() { alert('Test'); }</script>", scriptElement.text());
    }
}