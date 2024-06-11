package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello, World!", doc.getElementsByTag("h1").get(0).text());
    }
}