package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals(1, doc.select("html").size());
        assertEquals(1, doc.select("head").size());
        assertEquals(1, doc.select("title").size());
        assertEquals(1, doc.select("body").size());
        assertEquals(1, doc.select("p").size());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParse_invalidHtml() {
        String html = "<html><head><title>Test<title></head><body><p>Hello World</p></body></html>";
        Parser invalidParser = new Parser(html, "http://www.example.com", false);
        Document doc = invalidParser.parse();
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(1, doc.select("html").size());
        assertEquals(1, doc.select("head").size());
        assertEquals(1, doc.select("title").size());
        assertEquals(1, doc.select("body").size());
        assertEquals(1, doc.select("p").size());
        assertEquals("Hello World", doc.select("p").text());
        assertEquals(1, doc.select("comment").size());
        assertEquals("This is a comment", doc.select("comment").text());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(1, doc.select("html").size());
        assertEquals(1, doc.select("head").size());
        assertEquals(1, doc.select("title").size());
        assertEquals(1, doc.select("body").size());
        assertEquals(1, doc.select("p").size());
        assertEquals("Hello World", doc.select("p").text());
        assertEquals(1, doc.select("xml-decl").size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.select("xml-decl").text());
    }

    // Implement tests for other methods in the Parser class

}