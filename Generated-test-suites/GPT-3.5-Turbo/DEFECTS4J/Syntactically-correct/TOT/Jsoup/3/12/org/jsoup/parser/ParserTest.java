
package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void shouldParseHTMLIntoDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", doc.toString());
    }

    @Test
    public void shouldParseBodyFragmentIntoDocument() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.toString());
    }

    @Test
    public void shouldParseBodyFragmentWithRelaxedParsingEnabledIntoDocument() {
        String bodyHtml = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = parser.parseRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("<html><head></head><body><div>Test</div></body></html>", doc.toString());
    }

    @Test
    public void shouldParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<!-- This is a comment -->", doc.toString());
    }

    @Test
    public void shouldParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<![CDATA[This is CDATA]]>", doc.toString());
    }

    @Test
    public void shouldParseXmlDeclaration() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><h1>Hello</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><h1>Hello</h1>", doc.toString());
    }

    @Test
    public void shouldParseEndTag() {
        String html = "<h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<h1>Hello</h1></body></html>", doc.toString());
    }

    @Test
    public void shouldParseStartTag() {
        String html = "<h1>Hello</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<h1>Hello</h1>", doc.toString());
    }

    @Test
    public void shouldParseTextNode() {
        String html = "Hello";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello", doc.toString());
    }

    @Test
    public void shouldAddChildToParent() {
        String html = "<div>Test<span>Test</span></div>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("<div>Test<span>Test</span></div>", doc.toString());
    }
}

