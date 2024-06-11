package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "</body>\n" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());

        Element root = doc.childNodes().get(0);
        assertEquals(htmlTag, root.tag());

        assertEquals(1, root.childNodes().size());
        Element head = root.childNodes().get(0);
        assertEquals(headTag, head.tag());

        assertEquals(1, head.childNodes().size());
        Element title = head.childNodes().get(0);
        assertEquals(titleTag, title.tag());

        assertEquals(1, title.childNodes().size());
        TextNode titleText = title.childNodes().get(0);
        assertEquals("Test Page", titleText.text());

        assertEquals(1, root.childNodes().size());
        Element body = root.childNodes().get(0);
        assertEquals(bodyTag, body.tag());

        assertEquals(1, body.childNodes().size());
        Element h1 = body.childNodes().get(0);
        assertEquals("h1", h1.tagName());

        assertEquals(1, h1.childNodes().size());
        TextNode h1Text = h1.childNodes().get(0);
        assertEquals("Hello, World!", h1Text.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body>\n" +
                "<h1>Fragment Test</h1>\n";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());

        Element root = doc.childNodes().get(0);
        assertEquals(htmlTag, root.tag());

        assertEquals(1, root.childNodes().size());
        Element bodyFragment = root.childNodes().get(0);
        assertEquals(bodyTag, bodyFragment.tag());

        assertEquals(1, bodyFragment.childNodes().size());
        Element h1 = bodyFragment.childNodes().get(0);
        assertEquals("h1", h1.tagName());

        assertEquals(1, h1.childNodes().size());
        TextNode h1Text = h1.childNodes().get(0);
        assertEquals("Fragment Test", h1Text.text());
    }

    @Test
    public void testParseRelaxedBodyFragment() {
        String bodyHtml = "<body>\n" +
                "<div id=\"myDiv\">\n" +
                "</body>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        assertEquals(1, doc.childNodes().size());

        Element root = doc.childNodes().get(0);
        assertEquals(htmlTag, root.tag());

        assertEquals(1, root.childNodes().size());
        Element bodyFragment = root.childNodes().get(0);
        assertEquals(bodyTag, bodyFragment.tag());

        assertEquals(1, bodyFragment.childNodes().size());
        Element div = bodyFragment.childNodes().get(0);
        assertEquals("div", div.tagName());

        assertNull(div.tag().getId()); // no id attribute set
    }

    @Test(expected = AssertionError.class)
    public void testParseEmptyString() {
        Parser.parse("", "http://example.com");
    }

    @Test(expected = AssertionError.class)
    public void testParseNull() {
        Parser.parse(null, "http://example.com");
    }
}