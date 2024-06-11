package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", doc.html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html><head></head><body><h1>Hello World!</h1></body></html>", doc.html());
    }
    
    @Test
    public void testParseAttribute() {
        String attributeHtml = "<div id=\"test\" class=\"example\"></div>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(attributeHtml, baseUri);
        
        Element div = doc.select("div").first();
        assertEquals("test", div.attr("id"));
        assertEquals("example", div.attr("class"));
    }
    
    @Test
    public void testParseTextNode() {
        String textNodeHtml = "<p>Hello World!</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(textNodeHtml, baseUri);
        
        Element p = doc.select("p").first();
        assertEquals("Hello World!", p.text());
    }
    
    @Test
    public void testParseCdata() {
        String cdataHtml = "<script><![CDATA[var x = 5;]]></script>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(cdataHtml, baseUri);
        
        Element script = doc.select("script").first();
        assertEquals("var x = 5;", script.data());
    }
}