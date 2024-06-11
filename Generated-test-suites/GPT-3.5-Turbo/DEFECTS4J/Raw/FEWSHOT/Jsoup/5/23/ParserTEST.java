package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Hello World</title></head><body><h1>Welcome</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Hello World", doc.title());

        Elements h1Elements = doc.getElementsByTag("h1");
        assertEquals(1, h1Elements.size());
        assertEquals("Welcome", h1Elements.first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome</h1><p>This is a paragraph</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());

        Elements h1Elements = doc.getElementsByTag("h1");
        assertEquals(1, h1Elements.size());
        assertEquals("Welcome", h1Elements.first().text());

        Elements pElements = doc.getElementsByTag("p");
        assertEquals(1, pElements.size());
        assertEquals("This is a paragraph", pElements.first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Welcome</h1><p>This is a paragraph";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());

        Elements h1Elements = doc.getElementsByTag("h1");
        assertEquals(1, h1Elements.size());
        assertEquals("Welcome", h1Elements.first().text());

        Elements pElements = doc.getElementsByTag("p");
        assertEquals(1, pElements.size());
        assertEquals("This is a paragraph", pElements.first().text());
    }
}