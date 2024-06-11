package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TokenQueue;

import java.util.LinkedList;


public class ParserTest {
     @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Welcome</h1>\n" +
                "<p>This is a test page</p>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://www.example.com";
        Document result = Parser.parse(html, baseUri);
        
        assertEquals("Test Page", result.title());
        assertEquals("https://www.example.com", result.baseUri());
        
        Element body = result.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Welcome", h1.text());
        
        Element p = body.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals("This is a test page", p.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome</h1>\n" +
                "<p>This is a test page</p>\n";
        String baseUri = "https://www.example.com";
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNull(result.title());
        assertEquals("https://www.example.com", result.baseUri());
        
        Element body = result.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Welcome", h1.text());
        
        Element p = body.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals("This is a test page", p.text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Welcome</h2>\n" +
                "<p>This is a test page</a>\n";
        String baseUri = "https://www.example.com";
        Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNull(result.title());
        assertEquals("https://www.example.com", result.baseUri());
        
        Element body = result.body();
        assertNotNull(body);
        
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Welcome", h1.text());
        
        Element p = body.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals("This is a test page", p.text());
    }
}