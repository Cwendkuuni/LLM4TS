package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for Parser class.
 */
public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "<p id=\"p1\">This is a paragraph.</p>\n" +
                "<a href=\"https://www.example.com\">Link</a>\n" +
                "<script src=\"script.js\"></script>\n" +
                "</body>\n" +
                "</html>";
        parser = new Parser(html, "http://example.com");
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals(htmlTag, doc.tagName());
        Element html = doc.body();
        assertEquals(htmlTag, html.tagName());
        assertEquals(1, html.childNodeSize());

        Element h1 = html.getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());

        Element p = html.getElementsByTag("p").first();
        assertEquals("This is a paragraph.", p.text());

        Element link = html.getElementsByTag("a").first();
        assertEquals("Link", link.text());
        assertEquals("https://www.example.com", link.absUrl("href"));

        assertNull(parser.parse()); // Test null input
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p id=\"p1\">This is a paragraph.</p>", "http://example.com");
        Element body = doc.body();
        assertEquals(1, body.childNodeSize());

        Element p = body.getElementsByTag("p").first();
        assertEquals("This is a paragraph.", p.text());

        assertNull(Parser.parseBodyFragment(null, "http://example.com")); // Test null input
        assertNull(Parser.parseBodyFragment("", "http://example.com")); // Test empty input
    }

    @Test
    public void testParseComment() {
        parser = new Parser("<!DOCTYPE html>\n" +
                "<!--\n" +
                "This is a comment\n" +
                "-->\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>", "http://example.com");

        Document doc = parser.parse();
        Element comment = doc.body().child(0);
        assertEquals("This is a comment", comment.text());

        assertNull(parser.parseComment()); // Test empty comment
    }

    @Test
    public void testParseXmlDecl() {
        parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n" +
                "<html lang=\"en\">\n" +
                "</html>", "http://example.com");

        Document doc = parser.parse();
        XmlDeclaration xmlDecl = doc.head().child(0);
        assertEquals("about:legacy-compat", xmlDecl.systemId());

        assertNull(parser.parseXmlDecl()); // Test no XML declaration
    }

    @Test
    public void testParseEndTag() {
        parser = new Parser("<p id=\"p1\">This is a paragraph.</p>\n" +
                "<p><!-- This is a comment --></p>\n" +
                "</p>", "http://example.com");

        Document doc = parser.parse();
        Element p1 = doc.body().getElementsByTag("p").get(0);
        assertEquals(1, p1.childNodeSize());

        Element p2 = doc.body().getElementsByTag("p").get(1);
        assertEquals(0, p2.childNodeSize());

        assertNull(parser.parseEndTag("notag")); // Test invalid tag name
    }

    @Test
    public void testParseStartTag() {
        parser = new Parser("<p id=\"p1\">This is a paragraph.</p>\n" +
                "<h1>Hello World</h1>\n" +
                "<p>\n" +
                "<a href=\"https://www.example.com\">Link</a>\n" +
                "</p>\n" +
                "<script src=\"script.js\"></script>", "http://example.com");

        Document doc = parser.parse();
        Element p1 = doc.body().getElementsByTag("p").get(0);
        assertEquals(1, p1.childNodeSize());

        Element h1 = doc.body().getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());

        Element link = doc.body().getElementsByTag("a").first();
        assertEquals("Link", link.text());
        assertEquals("https://www.example.com", link.absUrl("href"));

        assertNull(parser.parseStartTag("<invalidtag>")); // Test invalid tag name
    }

    @Test
    public void testParseAttribute() {
        parser = new Parser("<p id=\"p1\" class=\"class1 class2\" title=\"Title\"><a href=\"https://www.example.com\">Link</a></p>", "http://example.com");

        Document doc = parser.parse();
        Element p = doc.body().getElementsByTag("p").first();
        Map<String, String> attributes = p.attributes();

        assertEquals("p", p.tagName());
        assertEquals("class1 class2", StringUtils.join(attributes.keySet(), " "));
        assertEquals("Title", attributes.get("title"));
        assertEquals("", attributes.get("id"));

        assertNull(parser.parseAttribute()); // Test empty tag
    }

    @Test
    public void testParseTextNode() {
        parser = new Parser("<p id=\"p1\">This is a paragraph.</p>", "http://example.com");

        Document doc = parser.parse();
        Element p = doc.body().getElementsByTag("p").first();
        TextNode textNode = (TextNode) p.child(0);

        assertEquals("This is a paragraph.", textNode.text());

        assertNull(parser.parseTextNode()); // Test empty text node
    }

    @Test
    public void testParseCdata() {
        parser = new Parser("<script>\n" +
                "function test() {\n" +
                "alert(\"Hello, World!\");\n" +
                "}\n" +
                "</script>", "http://example.com");

        Document doc = parser.parse();
        TextNode cdataNode = (TextNode) doc.body().child(0);

        assertEquals("<script>\n" +
                "function test() {\n" +
                "alert(\"Hello, World!\");\n" +
                "}\n" +
                "</script>", cdataNode.text());

        assertNull(parser.parseCdata()); // Test empty CDATA
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBaseUri() {
        parser = new Parser("<p id=\"p1\">This is a paragraph.</p>", null);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        parser = new Parser("", "http://example.com");
        assertThrows(IllegalArgumentException.class, parser::parse);
    }
}