package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test Paragraph <a href=\"https://example.com\">Link</a></p>" +
                "</body></html>";

        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element root = doc.body();
        assertEquals("Test Heading", root.getElementsByTag("h1").text());
        assertEquals("Test Paragraph Test Paragraph ", root.getElementsByTag("p").first().text());
        assertEquals("Link", root.getElementsByTag("a").text());
        assertEquals("https://example.com", root.getElementsByTag("a").attr("href"));
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<p>Test Paragraph <a href=\"https://example.com\">Link</a></p>";

        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parseBodyFragment(html, "http://example.com/");

        Element root = doc.body();
        assertEquals("Test Paragraph Test Paragraph ", root.getElementsByTag("p").text());
        assertEquals("Link", root.getElementsByTag("a").text());
        assertEquals("https://example.com", root.getElementsByTag("a").attr("href"));
    }

    @Test
    public void testParseWithRelaxedParsing() throws IOException {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "<!-- comment -->\n" +
                "<p>Test Paragraph <script type=\"text/javascript\">//<![CDATA[\n alert('Hello World');\n//]]>\n</script>\n" +
                "<p>Test Paragraph</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, "http://example.com/", true);
        Document doc = parser.parse();

        Element root = doc.body();
        Element div = root.getElementsByTag("div").first();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Paragraph Test Paragraph ", div.getElementsByTag("p").get(1).text());
        assertTrue(div.getElementsByTag("script").size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser parser = new Parser(null, "http://example.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        Parser parser = new Parser("<html></html>", null);
    }

    @Test
    public void testParseWithEmptyInput() {
        Parser parser = new Parser("", "http://example.com/");
        Document doc = parser.parse();
        assertTrue(doc.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag() {
        String invalidHtml = "<html><x";
        Parser parser = new Parser(invalidHtml, "http://example.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidEndTag() {
        String invalidHtml = "<html><body></head>";
        Parser parser = new Parser(invalidHtml, "http://example.com/");
    }

    @Test
    public void testParseInvalidTagName() {
        String invalidHtml = "<html><x></x>";
        Parser parser = new Parser(invalidHtml, "http://example.com/");
        Document doc = parser.parse();
        assertTrue(doc.body().getElementsByTag("x").isEmpty());
    }

    @Test
    public void testParseAttributeWithQuotes() {
        String html = "<p attr=\"value \"with quotes=\'value\'></p>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element p = doc.getElementsByTag("p").first();
        assertEquals("value ", p.attr("attr"));
        assertEquals("value'", p.attr("with quotes"));
    }

    @Test
    public void testParseAttributeWithNoQuotes() {
        String html = "<p attr=value></p>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element p = doc.getElementsByTag("p").first();
        assertEquals("value", p.attr("attr"));
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World')]]></script>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element script = doc.getElementsByTag("script").first();
        assertEquals("alert('Hello World')", script.text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element comment = doc.getElementsByTag("comment").first();
        assertEquals("This is a comment ", comment.text());
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<declare xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"/>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
        assertEquals("1.0", xmlDeclaration.attr("version"));
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
        assertTrue(xmlDeclaration.attr("standalone").equalsIgnoreCase("yes"));
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href=\"/test/\"/>";
        Parser parser = new Parser(html, "http://example.com/test/page1.html");
        Document doc = parser.parse();
        assertEquals("/test/", doc.baseUri());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src=\"image.png\" />";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element img = doc.getElementsByTag("img").first();
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<div><p>Test Paragraph</p></div>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element div = doc.getElementsByTag("div").first();
        assertEquals(1, div.children().size());
        Element p = div.getElementsByTag("p").first();
        assertEquals("Test Paragraph", p.text());
    }

    @Test
    public void testParseInvalidImplicitParent() {
        String html = "<p><p>Invalid</p></p>";
        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();
        Element p1 = doc.getElementsByTag("p").get(0);
        assertEquals(1, p1.children().size());
        Element p2 = p1.getElementsByTag("p").first();
        assertEquals("Invalid", p2.text());
    }

    @Test
    public void testParseNestedElements() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<ul>" +
                "<li>List Item 1</li>" +
                "<li><a href=\"https://example.com\">Link</a></li>" +
                "</ul>" +
                "</body></html>";

        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element root = doc.body();
        assertEquals("Test Heading", root.getElementsByTag("h1").text());
        List<Element> list = root.getElementsByTag("li");
        assertEquals(2, list.size());
        assertEquals("List Item 1", list.get(0).text());
        assertEquals("Link", list.get(1).getElementsByTag("a").text());
        assertEquals("https://example.com", list.get(1).getElementsByTag("a").attr("href"));
    }

    @Test
    public void testParseInvalidEndTagPlacement() {
        String html = "<html><body><p><p><p>\n</body></html>";
        Parser parser = new Parser(html, "http://example.com/");
        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseSelfClosingTags() {
        String html = "<html><body>" +
                "<img src=\"image.png\" alt=\"Alt Text\" />" +
                "<input type=\"text\" name=\"Name\" />" +
                "<p>Test Paragraph</p>" +
                "</body></html>";

        Parser parser = new Parser(html, "http://example.com/");
        Document doc = parser.parse();

        Element root = doc.body();
        assertEquals(3, root.getElementsByTag("img").size());
        assertEquals(1, root.getElementsByTag("input").size());
        assertEquals(1, root.getElementsByTag("p").size());

        Element img1 = root.getElementsByTag("img").get(0);
        assertEquals("image.png", img1.attr("src"));
        assertEquals("Alt Text", img1.attr("alt"));
        Extension attribute = img1.tag().attributes().get("alt");
        assertEquals("Alt Text", attribute.getValue());

        Element img2 = root.getElementsByTag("img").get(1);
        assertTrue(img2.isEmpty());

        Element input = root.getElementsByTag("input").first();
        assertEquals("text", input.tag().getName());
        assertEquals("Name", input.attr("name"));
        assertTrue(input.isEmpty());

        Element p = root.getElementsByTag("p").first();
        assertEquals("Test Paragraph", p.text());
    }
}