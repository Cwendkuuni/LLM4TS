package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

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

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Hello World", doc.getElementsByTag("h1").text());
        assertEquals("Test", doc.title().text());
        assertEquals(baseUri, doc.baseUri());

        Element body = doc.getElementsByTag("body").first();
        assertEquals(3, body.childSize());
        assertEquals("h1", body.getElementsByTag("h1").tagName());

        Element title = doc.getElementsByTag("title").first();
        assertEquals(1, title.childSize());
        assertEquals("Test", title.text());

        assertEquals(6, doc.numChildren());
        assertEquals(1, doc.child(0).childSize());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body>\n" +
                "<p>Test</p>\n";

        Parser parser = new Parser(bodyHtml, "http://www.example.com", true);
        Document doc = parser.parse();

        assertEquals("Test", doc.body().text());

        Element p = doc.getElementsByTag("p").first();
        assertEquals("Test", p.text());
        assertEquals(1, doc.numChildren());
        assertEquals("body", doc.tagName());
    }

    @Test
    public void testParseRelaxed() {
        String html = "<html>\n" +
                "<body>\n" +
                "<div class='foo'><!-- comment -->\n" +
                "<p>Test</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test", doc.body().getElementsByClass("foo").first().getElementsByTag("p").text());
        assertEquals(1, doc.body().getElementsByClass("foo").size());
        assertEquals("div", doc.body().getElementsByClass("foo").first().tagName());
        assertEquals(1, doc.numChildren());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testInvalidInput() {
        String html = "<html>\n" +
                "<body>\n" +
                "<-- Hello World -->\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);

        assertNull(parser.parse());
    }

    @Test
    public void testXmlDeclaration() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        XmlDeclaration xmlDeclaration = doc.getXmlDeclaration();
        assertEquals("xmlns='http://www.w3.org/1999/xhtml'", xmlDeclaration.xmlDeclData());
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.encoding());
        assertEquals(false, xmlDeclaration.standalone());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("", doc.body().text());
        assertEquals(0, doc.body().childSize());
        assertEquals(1, doc.numChildren());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseDocType() {
        String html = "<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">\n" +
                "<html xmlns='http://www.w3.org/1999/xhtml'>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        DocumentType doctype = doc.doctype();
        assertEquals("html", doctype.name());
        assertEquals("PUBLIC", doctype.publicId());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.systemId());
    }

    @Test
    public void testParseComment() {
        String html = "<html>\n" +
                "<!-- Test Comment -->\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.numChildren());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.head().childSize());
        assertEquals(0, doc.body().childSize());

        Comment comment = doc.head().getElementsByClass("comment").first();
        assertEquals("Test Comment", comment.data());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html>\n" +
                "<head>\n" +
                "<base href='/path/to/new/'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<a href='./link.html'></a>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("/path/to/new/link.html", doc.baseUri());
        assertEquals("link.html", doc.getElementsByTag("a").first().absUrl("href"));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html>\n" +
                "<body>\n" +
                "<div>\n" +
                "<p>Test</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element div = doc.body().getElementsByTag("div").first();
        assertEquals(1, div.childSize());
        assertEquals("p", div.getElementsByTag("p").tagName());

        Element implicitParent = div.parent();
        assertEquals("body", implicitParent.tagName());
        assertEquals("Test", implicitParent.getElementsByTag("p").text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test", doc.body().getElementsByTag("p").text());
    }

    @Test
    public void testParseCDATA() {
        String html = "<html>\n" +
                "<body>\n" +
                "<script><![CDATA[function test() { alert('Hello World'); }]]></script>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        TextNode scriptNode = doc.getElementsByTag("script").first().child(0);
        assertEquals("function test() { alert('Hello World'); }", scriptNode.text());
    }

    @Test
    public void testParseUnknownTag() {
        String html = "<html><zim><foo></foo></zim></html>";

        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);

        assertFalse(parser.parse() instanceof Document);
    }
}