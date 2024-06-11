package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        
        Element body = doc.body();
        Element h1 = body.select("h1").first();
        assertEquals("Hello World", h1.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        
        Element body = doc.body();
        Element div = body.select("div").first();
        Element p = div.select("p").first();
        assertEquals("Test", p.text());
    }
    
}