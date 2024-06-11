
package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Heading", doc.getElementsByTag("h1").first().text());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Paragraph", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Heading</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Heading", doc.getElementsByTag("h1").first().text());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Paragraph", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- this is a comment -->";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        Comment comment = (Comment) doc.childNode(0);

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals(" this is a comment ", comment.getData());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<html><head><title>Test</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>]]>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        TextNode cdata = (TextNode) doc.childNode(0);

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>", cdata.getWholeText());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        XmlDeclaration xmlDecl = (XmlDeclaration) doc.childNode(0);

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("1.0", xmlDecl.attr("version"));
        assertEquals("UTF-8", xmlDecl.attr("encoding"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>Paragraph</p>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        Element p = doc.getElementsByTag("p").first();

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Paragraph", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div class=\"container\"><h1>Heading</h1><p>Paragraph</p></div>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        Element div = doc.getElementsByClass("container").first();
        Element h1 = div.getElementsByTag("h1").first();
        Element p = div.getElementsByTag("p").first();

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Heading", h1.text());
        assertEquals("Paragraph", p.text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Paragraph</p>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        TextNode textNode = (TextNode) doc.childNode(0).childNode(0);

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Paragraph", textNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"http://www.example.com\"></a>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        Element a = doc.getElementsByTag("a").first();

        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("http://www.example.com", a.absUrl("href"));
    }

    @Test
    public void testParseEmptyTag() {
        String html = "<br>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        Element br = doc.getElementsByTag("br").first();

        assertEquals("http://www.example.com", doc.baseUri());
        assertTrue(br.tag().isEmpty());
    }
}
