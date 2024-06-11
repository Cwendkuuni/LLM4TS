package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.parser.Parser.*;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Test cases for the Parser class.
 */
public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = parse(html, "http://example.com");
        Element root = doc.body(); // the root is the body in this case
        assertEquals("http://example.com", doc.baseUri());
        assertTrue(root.children().isEmpty());
    }

    @Test
    public void testParseBasicHtml() {
        String html = "<html>\n" +
                "<head><title>Test Page</title></head>\n" +
                "<body>\n" +
                "<h1>Welcome to Test Page</h1>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        Document doc = parse(html, "http://example.com");

        Element root = doc.body();
        assertEquals(1, root.children().size());
        Element h1 = root.children().get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Welcome to Test Page", h1.text());

        List<Element> pList = root.getElementsByTag("p");
        assertEquals(1, pList.size());
        Element p = pList.get(0);
        assertEquals("p", p.tagName());
        assertEquals("This is a test page.", p.text());

        assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello</h1></body>";
        Document doc = parseBodyFragment(html, "http://example.com");

        Element root = doc.body();
        assertEquals(1, root.children().size());
        Element h1 = root.children().get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());

        assertEquals("", doc.head().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseTag() {
        String html = "<head><base href=\"/foo/\"></head>\n" +
                "<body><h1>Hello</h1></body>";
        Document doc = parseBodyFragment(html, "http://example.com");

        Element root = doc.body();
        assertEquals(1, root.children().size());
        Element h1 = root.children().get(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());

        assertEquals("/foo/", doc.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBaseUri() {
        parse("<html></html>", "");
    }

    @Test
    public void testParseNonConformingHtml() {
        String html = "<html><body><script>alert('foo')</script></body></html>";
        Document doc = parse(html, "http://example.com");

        Element root = doc.body();
        assertEquals(1, root.children().size());
        Element script = root.children().get(0);
        assertEquals("script", script.tagName());
        assertTrue(script.hasAttr("type"));
        assertEquals("application/javascript", script.attr("type"));

        // check that the script is executed as part of parsing
        String alertMessage = "";
        try {
            alertMessage = (String) new Object() {}.getClass().getMethod("toString").invoke(new StringReader(IOUtils.toString(doc.text())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals("foo", alertMessage);
    }
}