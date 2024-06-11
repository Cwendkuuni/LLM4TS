package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JsoupTest {
    @Test
    public void testParseWithBaseUri() {
        Document document = Jsoup.parse("<html></html>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseWithBaseUriAndParser() {
        Document document = Jsoup.parse("<html></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document document = Jsoup.parse("<html></html>");
        assertNotNull(document);
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUriAndParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document document = Jsoup.parseBodyFragment("<p>Text</p>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        Document document = Jsoup.parseBodyFragment("<p>Text</p>");
        assertNotNull(document);
    }

    @Test
    public void testParseURLWithTimeout() throws IOException {
        URL url = new URL("http://example.com");
        Document document = Jsoup.parse(url, 5000);
        assertNotNull(document);
    }

    @Test
    public void testCleanWithBaseUri() {
        String cleanedHtml = Jsoup.clean("<p>Text</p>", "http://example.com", Whitelist.basic());
        assertNotNull(cleanedHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String cleanedHtml = Jsoup.clean("<p>Text</p>", Whitelist.basic());
        assertNotNull(cleanedHtml);
    }

    @Test
    public void testCleanWithBaseUriAndOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        String cleanedHtml = Jsoup.clean("<p>Text</p>", "http://example.com", Whitelist.basic(), outputSettings);
        assertNotNull(cleanedHtml);
    }
    
    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid("<p>Text</p>", Whitelist.basic());
        assertTrue(valid);
    }
    
    @Test
    public void testIsNotValid() {
        boolean valid = Jsoup.isValid("<script>alert('Hello');</script>", Whitelist.basic());
        assertFalse(valid);
    }
}