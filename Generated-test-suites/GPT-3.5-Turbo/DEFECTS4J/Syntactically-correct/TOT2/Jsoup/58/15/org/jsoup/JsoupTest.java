package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        Document doc = Jsoup.parse("<html><body>Hello World</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithParser() {
        Document doc = Jsoup.parse("<html><body>Hello World</body></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document doc = Jsoup.parse("<html><body>Hello World</body></html>");
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
    }

    // More test cases can be added for other methods in Jsoup class

}