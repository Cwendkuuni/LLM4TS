package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Testing parsing with implicit tags</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        
        assertEquals("Testing parsing with implicit tags", doc.body().text());
    }

    // Add more test cases for other methods as needed

}