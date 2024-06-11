package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.Jsoup;
import org.jsoup.Document;
import org.jsoup.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
    }

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><body><a href='/about'>About</a></body></html>";
        Document document = Jsoup.parse(html, "http://example.com");
        assertNotNull(document.select("a[href='/about']").first());
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseStringEmpty() {
        Document document = Jsoup.parse("");
        assertNotNull(document);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        assertNotNull(document);
    }

    @Test
    public void testParseFile() {
        File file = new File("src/test/resources/test.html");
        Document document = null;
        try {
            document = Jsoup.parse(file, "UTF-8");
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFileWithBaseUri() {
        File file = new File("src/test/resources/test.html");
        Document document = null;
        try {
            document = Jsoup.parse(file, "UTF-8", "http://example.com");
            assertNotNull(document.select("a[href='/about']").first());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        File file = new File("src/test/resources/test.html");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Document document = Jsoup.parse(inputStream, "UTF-8");
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testParseInputStreamWithBaseUri() {
        File file = new File("src/test/resources/test.html");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document.select("a[href='/about']").first());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullString() {
        Document document = Jsoup.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyString() {
        Document document = Jsoup.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidUrl() {
        Document document = assertThrows(IllegalArgumentException.class, () -> Jsoup.parse("invalid:/url"));
    }

    @Test(expected = MalformedURLException.class)
    public void testParseMalformedUrl() {
        Document document = assertThrows(MalformedURLException.class, () -> Jsoup.parse("malformed://url"));
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src='/scripts/bad.js'></script><div id='content'>Hello World</div>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertNotNull(cleanHtml);
        assertNotNull(cleanHtml.matches(".*Hello World.*"));
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<script src='/scripts/bad.js'></script><a href='/about'>About</a><div id='content'>Hello World</div>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "http://example.com", whitelist);
        assertNotNull(cleanHtml);
        assertNotNull(cleanHtml.matches(".*<a[^>]*href='/about'.*>About<\\/a>.*Hello World.*"));
    }

    @Test
    public void testCleanWithDocumentOutputSettings() {
        String bodyHtml = "<script src='/scripts/bad.js'></script><div id='content'><p>Hello World</p></div>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertNotNull(cleanHtml.matches(".*<div>.*<p>([\\s\\S]*?)<\\/p>.*Hello World.*"));
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullBodyHtml() {
        String cleanHtml = Jsoup.clean(null, "", Whitelist.simpleText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanEmptyBodyHtml() {
        String cleanHtml = Jsoup.clean("", "", Whitelist.simpleText());
    }
}