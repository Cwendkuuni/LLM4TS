package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Element body = doc.body();
        Element h1 = body.selectFirst("h1");
        assertEquals("Hello World!", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element body = doc.body();
        Element h1 = body.selectFirst("h1");
        assertEquals("Hello World!", h1.text());
    }
}