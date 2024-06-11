package org.jsoup;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Jsoup.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><p>Hello, Jsoup!</p></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document doc = Jsoup.parse(html, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Hello, Jsoup!", doc.body().text());
    }

    // Add more test cases for other methods
}