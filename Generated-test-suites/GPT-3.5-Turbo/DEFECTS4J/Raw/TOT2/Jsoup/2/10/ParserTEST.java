
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().children().size() == 1);
    }

    // Add more test cases for other methods as needed
}

