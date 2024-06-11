package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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

public class JsoupTest {
    @Test
    public void testParse() {
        Document document = Jsoup.parse("<html></html>", "http://example.com");

        assertEquals("", document.title());
    }

    @Test
    public void testParseWithParser() {
        Document document = Jsoup.parse("<html></html>", "http://example.com", Parser.xmlParser());

        assertEquals("", document.title());
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document document = Jsoup.parse("<html></html>");

        assertEquals("", document.title());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");

        assertEquals("http://example.com", connection.url());
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        Document document = Jsoup.parse(new File("file.html"), "UTF-8", "http://example.com");

        assertEquals("", document.title());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        Document document = Jsoup.parse(new File("file.html"), "UTF-8");

        assertEquals("", document.title());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("file.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com");

        assertEquals("", document.title());
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("file.html");
        Document document = Jsoup.parse(inputStream, "UTF-8");

        assertEquals("", document.title());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document document = Jsoup.parseBodyFragment("<div></div>", "http://example.com");

        assertTrue(document.body().hasText());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        Document document = Jsoup.parseBodyFragment("<div></div>");

        assertTrue(document.body().hasText());
    }

    @Test
    public void testParseURL() throws IOException {
        Document document = Jsoup.parse(new URL("http://example.com"), 5000);

        assertEquals("", document.title());
    }

    @Test
    public void testCleanWithStringAndBaseUriAndWhitelist() {
        String result = Jsoup.clean("<div></div>", "http://example.com", Whitelist.basic());

        assertEquals("", result);
    }

    @Test
    public void testCleanWithStringAndWhitelist() {
        String result = Jsoup.clean("<div></div>", Whitelist.basic());

        assertEquals("", result);
    }

    @Test
    public void testCleanWithStringAndBaseUriAndWhitelistAndOutputSettings() {
        String result = Jsoup.clean("<div></div>", "http://example.com", Whitelist.basic(), new Document.OutputSettings());

        assertEquals("", result);
    }

    @Test
    public void testIsValidWithStringAndWhitelist() {
        boolean valid = Jsoup.isValid("<div></div>", Whitelist.basic());

        assertTrue(valid);
    }
}