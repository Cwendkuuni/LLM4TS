package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
        assertEquals(baseUri, doc.baseUri());
    }

    // More test methods for other public methods in Jsoup class

}