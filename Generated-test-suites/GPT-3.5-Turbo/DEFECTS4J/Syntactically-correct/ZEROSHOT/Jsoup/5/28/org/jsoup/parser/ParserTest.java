package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.LinkedList;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "";
        Document doc = Parser.parse(html, baseUri);

        // Test that the parsed document is not null
        assertNotNull(doc);

        // Test that the document has the correct HTML tag
        assertEquals("html", doc.tagName());

        // Test that the document has the correct number of child elements
        assertEquals(2, doc.children().size());

        // Test that the document has the correct head
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        // Test that the document has the correct title
        Element title = head.select("title").first();
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        // Test that the document has the correct body
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        // Test that the body has the correct h1
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Test that the parsed document is not null
        assertNotNull(doc);

        // Test that the document has the correct number of child elements
        assertEquals(1, doc.children().size());

        // Test that the document has the correct body
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        // Test that the body has the correct h1
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        // Test that the parsed document is not null
        assertNotNull(doc);

        // Test that the document has the correct number of child elements
        assertEquals(1, doc.children().size());

        // Test that the document has the correct body
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        // Test that the body has the correct h1
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }
}