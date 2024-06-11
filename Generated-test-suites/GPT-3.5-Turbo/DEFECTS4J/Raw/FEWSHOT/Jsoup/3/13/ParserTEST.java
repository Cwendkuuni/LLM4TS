package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html>" +
                        "<head>" +
                            "<title>Title</title>" +
                        "</head>" +
                        "<body>" +
                            "<h1>Hello World!</h1>" +
                        "</body>" +
                      "</html>";
                      
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertEquals("Title", document.title());
        assertEquals("Hello World!", document.body().select("h1").first().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("Hello World!", document.body().select("h1").first().text());
    }
}