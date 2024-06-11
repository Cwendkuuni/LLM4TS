package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><h1>Hello World</h1><p>Test Parser</p></div></body></html>";
        Parser parser = new Parser(html, "https://example.com");
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.rootElement());
        assertEquals(htmlTag, doc.head().parent());
        assertEquals(headTag, doc.head().tag());
        assertEquals(titleTag, doc.head().child(0).tag());
        assertEquals("Test", doc.head().child(0).text());

        assertEquals(bodyTag, doc.body().tag());
        assertEquals(4, doc.body().childNodeSize());

        Element div = doc.body().child(0);
        assertEquals(divTag, div.tag());
        assertEquals(2, div.childNodeSize());

        Element h1 = div.child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Hello World", h1.text());

        Element p = div.child(1);
        assertEquals(pTag, p.tag());
        assertEquals("Test Parser", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><h1>Hello World</h1><p>Test Parser</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.rootElement());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(2, doc.body().childNodeSize());

        Element div = doc.body().child(0);
        assertEquals(divTag, div.tag());
        assertEquals(2, div.childNodeSize());

        Element h1 = div.child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Hello World", h1.text());

        Element p = div.child(1);
        assertEquals(pTag, p.tag());
        assertEquals("Test Parser", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><foobar><h1>Hello World</h1><p>Test Parser</p></foobar></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.rootElement());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(1, doc.body().childNodeSize());
        Element div = doc.body().child(0);
        assertEquals(divTag, div.tag());
        assertEquals(1, div.childNodeSize());
        Element foobar = div.child(0);
        assertEquals("foobar", foobar.tagName());
        assertEquals(2, foobar.childNodeSize());

        Element h1 = foobar.child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Hello World", h1.text());

        Element p = foobar.child(1);
        assertEquals(pTag, p.tag());
        assertEquals("Test Parser", p.text());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        assertEquals(htmlTag, doc.rootElement());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseInvalidHtml() {
        String invalidHtml = "<html><body><script>alert('Test')<div>";
        try {
            Document doc = new Parser(invalidHtml, "https://example.com").parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><base href=\"/test/\"><head><title>Test</title></head><body><div><h1>Hello World</h1><p>Test Parser</p></div></body></html>";
        Parser parser = new Parser(html, "https://example.com");
        Document doc = parser.parse();

        assertEquals("https://example.com/test/Hello%20World.html", doc.url());
        assertEquals("https://example.com/test/Test%20Parser.html", doc.titleText());
    }
}