package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>\n" +
                "   Test Page\n" +
                "  </title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Hello World!\n" +
                "  </h1>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <h1>\n" +
                "  Hello World!\n" +
                " </h1>\n" +
                "</body>", doc.toString());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("", "", false);
        
        Attribute attribute1 = parser.parseAttribute();
        assertNull(attribute1);
        
        parser.tq.addFirst("key=value");
        Attribute attribute2 = parser.parseAttribute();
        assertEquals("Attribute [key=value]", attribute2.toString());
        
        parser.tq.addFirst("key");
        Attribute attribute3 = parser.parseAttribute();
        assertNull(attribute3);
    }
}