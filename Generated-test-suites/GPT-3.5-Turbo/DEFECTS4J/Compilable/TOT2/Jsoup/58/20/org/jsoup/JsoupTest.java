package org.jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }

    @Test
    public void testParseFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = Jsoup.parse(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (Exception e) {
            // Handle Exception
        }
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = Jsoup.parse(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (Exception e) {
            // Handle Exception
        }
    }

    @Test
    public void testParseURL() {
        try {
            URL url = new URL("http://example.com");
            int timeoutMillis = 5000;
            Document document = Jsoup.parse(url, timeoutMillis);
            assertNotNull(document);
        } catch (MalformedURLException e) {
            // Handle MalformedURLException
        } catch (Exception e) {
            // Handle other Exceptions
        }
    }

    @Test
    public void testClean() {
        String html = "<script>alert('XSS');</script>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(html, whitelist);
        assertEquals("<p></p>", cleanHtml);
    }

    // Add more test methods for other public methods in Jsoup class

}