package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        Element headNode = htmlNode.getPreviousSibling();
        Element titleNode = headNode.getFirstChild();

        assertEquals("html", htmlNode.tagName());
        assertEquals("head", headNode.tagName());
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());
        assertEquals("Hello World", htmlNode.getElementsByTag("h1").get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element htmlNode = doc.body();
        Element h1Node = htmlNode.getFirstChild();

        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());

        assertEquals(0, doc.head().childNodeSize());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        String bodyHtml = "<p>Hello <b>World</b></p>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com", true);

        Element htmlNode = doc.body();
        Element pNode = htmlNode.getFirstChild();
        Element bNode = pNode.getFirstChild();

        assertEquals("p", pNode.tagName());
        assertEquals("b", bNode.tagName());
        assertEquals("World", bNode.getChild(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html>\n" +
                "<!-- This is a comment -->\n" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");
        Element commentNode = doc.getElementsByTagName("comment").get(0);

        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">";

        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = doc.getElementsByTagName("xml-decl").get(0).getFirstChild();

        assertEquals("xml-decl", doc.getElementsByTagName("xml-decl").get(0).tagName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlDecl.attr("xmlns:xsi"));
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlDecl.attr("xmlns:xml"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello World</p></div>";

        Document doc = Parser.parse(html, "http://example.com");
        Element divNode = doc.getElementsByTagName("div").get(0);
        Element pNode = divNode.getFirstChild();

        assertEquals(1, divNode.childNodeSize());
        assertEquals(1, pNode.childNodeSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";

        Document doc = Parser.parse(html, "http://example.com");
        Element imgNode = doc.getElementsByTagName("img").get(0);

        assertEquals("img", imgNode.tagName());
        assertEquals("image.jpg", imgNode.attr("src"));
        assertEquals("Image", imgNode.attr("alt"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"page.html\">Link</a>";

        Document doc = Parser.parse(html, "http://example.com");
        Element aNode = doc.getElementsByTagName("a").get(0);

        assertEquals("href", aNode.attrName("href"));
        assertEquals("page.html", aNode.attr("href"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";

        Document doc = Parser.parse(html, "http://example.com");
        Element pNode = doc.getElementsByTagName("p").get(0);

        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World');]]></script>";

        Document doc = Parser.parse(html, "http://example.com");
        Element scriptNode = doc.getElementsByTagName("script").get(0);

        assertEquals("script", scriptNode.tagName());
        assertEquals("alert('Hello World');", scriptNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href=\"/new-folder/\">";

        Document doc = Parser.parse(html, "http://example.com");
        Element baseNode = doc.getElementsByTagName("base").get(0);

        assertEquals("base", baseNode.tagName());
        assertEquals("/new-folder/", baseNode.attr("href"));

        assertEquals("http://example.com/new-folder/", doc.baseUri());
    }
}