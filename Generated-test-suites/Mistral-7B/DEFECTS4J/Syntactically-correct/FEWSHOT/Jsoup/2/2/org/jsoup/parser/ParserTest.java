package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tag());
        assertNotNull(doc.head());
        assertEquals(titleTag, doc.head().tag());
        assertEquals("Test Page", doc.head().text());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodeSize());
        Element p = doc.body().child(0);
        assertEquals(pTag, p.tag());
        assertEquals("Hello, world!", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, fragment!</p>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tag());
        assertEquals(headTag, doc.head());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodeSize());
        Element p = doc.body().child(0);
        assertEquals(pTag, p.tag());
        assertEquals("Hello, fragment!", p.text());
    }

    @Test
    public void testParseEmpty() {
        String html = "";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(htmlTag, doc.tag());
        assertEquals(headTag, doc.head());
        assertEquals(bodyTag, doc.body());
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><p<script>alert('xss')</script>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);

        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected
        }

        assertEquals(htmlTag, parser.doc.tag());
        assertEquals(headTag, parser.doc.head());
        assertEquals(bodyTag, parser.doc.body());
        assertEquals(0, parser.doc.body().childNodeSize());
    }

    @Test
    public void testParseComment() {
        String html = "<html>\n" +
                "<!-- This is a comment -->\n" +
                "</html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(1, doc.childNodeSize());
        Element comment = doc.child(0);
        assertEquals(Comment.class, comment.getClass());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(1, doc.childNodeSize());
        Element xmlDecl = doc.child(0);
        assertEquals(XmlDeclaration.class, xmlDecl.getClass());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(htmlTag, doc.tag());
        assertEquals(headTag, doc.head());
        assertEquals(1, doc.body().childNodeSize());
        Element body = doc.body();
        assertEquals(bodyTag, body.tag());
        assertEquals(0, body.childNodeSize());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, world!</p></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodeSize());
        Element htmlEl = doc.child(0);
        assertEquals(htmlTag, htmlEl.tag());

        assertEquals(1, htmlEl.childNodeSize());
        Element headEl = htmlEl.child(0);
        assertEquals(headTag, headEl.tag());

        assertEquals(1, headEl.childNodeSize());
        Element titleEl = headEl.child(0);
        assertEquals(titleTag, titleEl.tag());
        assertEquals("Test Page", titleEl.text());

        assertEquals(1, doc.body().childNodeSize());
        Element bodyEl = doc.body();
        assertEquals(bodyTag, bodyEl.tag());

        assertEquals(1, bodyEl.childNodeSize());
        Element pEl = bodyEl.child(0);
        assertEquals(pTag, pEl.tag());
        assertEquals("Hello, world!", pEl.text());
    }

    @Test
    public void testParseAttributes() {
        String html = "<p id='foo' class='bar baz' title='Bazinga!'>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element pEl = doc.body().child(0);

        assertEquals(3, pEl.attributeSize());
        Map<String, String> attributes = pEl.attributes();
        assertEquals("foo", attributes.get("id"));
        assertEquals("bar baz", attributes.get("class"));
        assertEquals("Bazinga!", attributes.get("title"));
    }

    @Test
    public void testParseEmptyAttributes() {
        String html = "<p class='' />";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element pEl = doc.body().child(0);

        assertEquals(0, pEl.attributeSize());
    }

    @Test
    public void testParseDataNodes() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "<script data-src=\"script.js\"></script>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodeSize());
        Element htmlEl = doc.child(0);
        assertEquals(htmlTag, htmlEl.tag());

        assertEquals(1, htmlEl.childNodeSize());
        Element headEl = htmlEl.child(0);
        assertEquals(headTag, headEl.tag());

        assertEquals(1, headEl.childNodeSize());
        Element titleEl = headEl.child(0);
        assertEquals(titleTag, titleEl.tag());
        assertEquals("Test Page", titleEl.text());

        assertEquals(2, htmlEl.childNodeSize());
        Element bodyEl = htmlEl.child(1);
        assertEquals(bodyTag, bodyEl.tag());

        assertEquals(2, bodyEl.childNodeSize());
        Element pEl = bodyEl.child(0);
        assertEquals(pTag, pEl.tag());
        assertEquals("Hello, world!", pEl.text());

        assertEquals(1, bodyEl.childNodeSize());
        Element scriptEl = bodyEl.child(1);
        assertEquals("script", scriptEl.tagName());
        assertEquals(0, scriptEl.childNodeSize());
        assertEquals("script.js", scriptEl.attr("data-src"));
        assertEquals(null, scriptEl.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html>\n" +
                "<head>\n" +
                "<base href=\"/subdir/\">\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("/subdir/", doc.baseUri());
    }

    @Test
    public void testParseCdata() {
        String html = "<html>\n" +
                "<head>\n" +
                "<script><![CDATA[function foo() { alert('xss'); }]]></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodeSize());
        Element htmlEl = doc.child(0);
        assertEquals(htmlTag, htmlEl.tag());

        assertEquals(1, htmlEl.childNodeSize());
        Element headEl = htmlEl.child(0);
        assertEquals(headTag, headEl.tag());

        assertEquals(1, headEl.childNodeSize());
        Element scriptEl = headEl.child(0);
        assertEquals("script", scriptEl.tagName());

        assertEquals(1, scriptEl.childNodeSize());
        Element cdataEl = scriptEl.child(0);
        assertEquals("CDATA", cdataEl.tagName());
        assertEquals("function foo() { alert('xss'); }", cdataEl.text());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "<script>\n" +
                "function foo() {\n" +
                "  alert('xss');\n" +
                "}\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodeSize());
        Element htmlEl = doc.child(0);
        assertEquals(htmlTag, htmlEl.tag());

        assertEquals(1, htmlEl.childNodeSize());
        Element bodyEl = htmlEl.child(0);
        assertEquals(bodyTag, bodyEl.tag());

        assertEquals(2, bodyEl.childNodeSize());
        Element pEl = bodyEl.child(0);
        assertEquals(pTag, pEl.tag());
        assertEquals("Hello, world!", pEl.text());

        assertEquals(1, bodyEl.childNodeSize());
        Element scriptEl = bodyEl.child(1);
        assertEquals("script", scriptEl.tagName());

        assertEquals(1, scriptEl.childNodeSize());
        Element pElInsideScript = scriptEl.child(0);
        assertEquals(pTag, pElInsideScript.tag());
        assertEquals("function foo() { alert('xss'); }", pElInsideScript.text());
    }
}