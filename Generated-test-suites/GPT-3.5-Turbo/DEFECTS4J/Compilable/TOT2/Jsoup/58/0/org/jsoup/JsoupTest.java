package org.jsoup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><body><h1>Hello Jsoup!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.select("h1").size());
    }

    @Test
    public void testParseHtmlWithParser() {
        String html = "<html><body><h2>Hello Jsoup!</h2></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals(1, doc.select("h2").size());
    }

    @Test
    public void testParseHtmlWithoutBaseUri() {
        String html = "<html><body><h3>Hello Jsoup!</h3></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals(1, doc.select("h3").size());
    }

    // Additional test cases for other methods can be added here

}