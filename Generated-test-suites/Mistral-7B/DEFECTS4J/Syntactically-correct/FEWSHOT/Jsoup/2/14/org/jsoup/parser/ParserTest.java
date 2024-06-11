package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tagName());
        assertEquals(htmlTag, last(doc).tagName());

        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head().tagName());
        assertEquals(1, doc.head().childNodes().size());
        assertEquals(titleTag, doc.head().childNodes().get(0).tagName());
        assertEquals("Test", doc.head().childNodes().get(0).text());

        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tagName());
        assertEquals("Hello World", doc.body().childNodes().get(0).text());

        Document emptyDoc = Parser.parse("<>", null);
        assertEquals(htmlTag, emptyDoc.tagName());
        assertEquals(htmlTag, last(emptyDoc).tagName());
        assertEquals(0, emptyDoc.childNodes().size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        assertEquals(htmlTag, doc.tagName());
        assertEquals(htmlTag, last(doc).tagName());

        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(h1Tag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Hello World", doc.childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertTrue(doc.childNodes().get(0).childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", doc.childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ <p>This is a CDATA section</p> ]]></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertTrue(doc.childNodes().get(0).childNodes().get(0) instanceof TextNode);
        assertEquals("<p>This is a CDATA section</p>", doc.childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertTrue(doc.childNodes().get(0).childNodes().get(0) instanceof XmlDeclaration);
        assertEquals("xmlns:foo=\"bar\"", doc.childNodes().get(0).childNodes().get(0).toString());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("p", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(2, doc.childNodes().get(0).childNodes().size());
        assertEquals(headTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(1).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(1).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(1).childNodes().size());
        assertEquals(h1Tag, doc.childNodes().get(0).childNodes().get(1).childNodes().get(0).tagName());
        assertEquals("Hello World", doc.childNodes().get(0).childNodes().get(1).childNodes().get(0).text());

        html = "<html><body><img src=\"test.png\"></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("img", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertNotNull(doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes());
        assertEquals("test.png", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes().get("src").getValue());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><img src=\"test.png\" alt=\"Test Image\"></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("img", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertNotNull(doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes());
        assertEquals("test.png", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes().get("src").getValue());
        assertEquals("Test Image", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes().get("alt").getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("p", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());

        html = "<html><body><script>console.log('Hello');</script></body></html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("script", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertNotNull(doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes());
        assertEquals("console.log('Hello');", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<html><body><img src=\"test.png\" alt=\"Test Image\"/></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("img", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("test.png", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes().get("src").getValue());
        assertEquals("Test Image", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes().get("alt").getValue());
        assertNull(doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).childNodes());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href=\"/test\"><title>Test</title></head><body><a href=\"/about\">About</a></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals(headTag, doc.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals(2, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
        assertEquals("base", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("/test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).attributes().get("href").getValue());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().get(1).childNodes().size());
        assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).childNodes().get(1).tagName());
        assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(1).childNodes().get(0).text());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(1).childNodes().size());
        assertEquals(bodyTag, doc.childNodes().get(0).childNodes().get(1).tagName());
        assertEquals(1, doc.childNodes().get(0).childNodes().get(1).childNodes().size());
        assertEquals(aTag, doc.childNodes().get(0).childNodes().get(1).childNodes().get(0).tagName());
        assertEquals("/about", doc.childNodes().get(0).childNodes().get(1).childNodes().get(0).attributes().get("href").getValue());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body<p>Test</body>";
        Parser parser = new Parser(html, null, false);
        assertFalse(parser.parse() instanceof Document);

        html = "<html><body><script>console.log('Hello');</script></body></html><script>console.log('World');</script>";
        parser = new Parser(html, null, false);
        assertFalse(parser.parse() instanceof Document);

        html = "<html><body><script>console.log('Hello');</script><script>console.log('World');</script></body></html>";
        parser = new Parser(html, null, false);
        assertFalse(parser.parse() instanceof Document);
    }

    private Element last(Document doc) {
        return (Element) doc.childNodes().get(doc.childNodes().size() - 1);
    }
}