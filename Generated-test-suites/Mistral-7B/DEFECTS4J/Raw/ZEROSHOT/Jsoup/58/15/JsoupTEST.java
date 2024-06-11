package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() throws IOException {
        String html = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseString() throws IOException {
        String html = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        Document document = Jsoup.parse(html);
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(filePath), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(new File(filePath), charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new FileInputStream(new File(filePath)), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(new FileInputStream(new File(filePath)), charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String filePath = "test.html";
        Document document = Jsoup.parse(new FileInputStream(new File(filePath)));
        assertNotNull(document);
    }

    @Test
    public void testConnectAndGet() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        assertNotNull(document);
    }

    @Test
    public void testConnectAndPost() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).post();
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() throws IOException {
        String bodyHtml = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testClean() throws IOException {
        String bodyHtml = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Whitelist whitelist = Whitelist.relaxed();

        Document dirty = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Document clean = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertTrue(Jsoup.validate(clean.body().html(), parser, whitelist));
    }

    @Test
    public void testIsValid() throws IOException {
        String bodyHtml = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        Whitelist whitelist = Whitelist.relaxed();

        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidUrl() throws IOException {
        String url = "file://example.com";
        Jsoup.parse(url);
    }

    @Test
    public void testCleanWithOutputSettings() throws IOException {
        String bodyHtml = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Whitelist whitelist = Whitelist.relaxed();

        Document dirty = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        outputSettings.escapeMode(Document.EscapeMode.html);

        Document clean = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        Document cleanedDocument = Jsoup.parse(cleanHtml, parser);
        assertTrue(Jsoup.validate(cleanedDocument.body().html(), parser, whitelist));
    }
}