package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    @Test
    public void testParse() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>", "");
        assertEquals("Test", doc.title());
        assertEquals("<p>This is a test.</p>", doc.body().html());
    }

    @Test
    public void testParseWithBaseUri() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>", "https://www.example.com");
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseWithParser() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>", "", Parser.xmlParser());
        assertEquals("Test", doc.title());
        assertEquals("<p>This is a test.</p>", doc.body().html());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<p>This is a test.</p>", "");
        assertEquals("<p>This is a test.</p>", doc.body().html());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<p>This is a test.</p>", "https://www.example.com");
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertEquals(5000, con.timeout());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "https://www.example.com");
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = DataUtil.loadStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "https://www.example.com");
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = DataUtil.loadStream("test.xml");
        Document doc = Jsoup.parse(in, "UTF-8", "https://www.example.com", Parser.xmlParser());
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void testClean() {
        String cleanHtml = Jsoup.clean("<p>This is <b>unsafe</b> HTML</p>", "https://www.example.com", Whitelist.simpleText());
        assertEquals("<p>This is unsafe HTML</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.indentAmount(4);
        String cleanHtml = Jsoup.clean("<p>This is <b>unsafe</b> HTML</p>", "https://www.example.com", Whitelist.simpleText(), outputSettings);
        assertEquals("<p>This is unsafe HTML</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        boolean isValid = Jsoup.isValid("<p>This is <b>unsafe</b> HTML</p>", Whitelist.simpleText());
        assertFalse(isValid);
        isValid = Jsoup.isValid("<p>This is safe HTML</p>", Whitelist.simpleText());
        assertTrue(isValid);
    }
}