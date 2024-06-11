package org.jsoup.parser;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to the test page</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("Test Page", doc.title());
        assertEquals("<html><head><title>Test Page</title></head><body><h1>Welcome to the test page</h1></body></html>", doc.html());
    }
}