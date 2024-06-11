package org.jsoup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<a href=\"/index.html\">Home</a>";
        Document document = Jsoup.parse(html, "http://example.com");
        assertEquals("http://example.com/index.html", document.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document document = Jsoup.parse(html, "", parser);
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseWithEmptyString() {
        Document document = Jsoup.parse("");
        assertTrue(document.body().text().isEmpty());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }

    @Test
    public void testParseFile() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        Document document = Jsoup.parse(new File(filePath), charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        InputStream inputStream = new FileInputStream(new File(filePath));
        Document document = Jsoup.parse(inputStream, charsetName);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<a href=\"/index.html\">Home</a>";
        Document document = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("http://example.com/index.html", document.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com";
        Document document = Jsoup.parse(new URL(urlString), 5000);
        assertNotNull(document);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><head><script>alert('Hello World');</script></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertFalse(cleanHtml.contains("<script>"));
        Document cleanedDocument = Jsoup.parse(cleanHtml, baseUri);
        assertNull(cleanedDocument.select("script").first());
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<a href=\"index.html\" target=\"_blank\">Home</a>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, baseUri);
        assertSame(baseUri, cleanHtml.substring(0, baseUri.length()));
        Document cleanedDocument = Jsoup.parse(cleanHtml, baseUri);
        Element linkElement = cleanedDocument.selectFirst("a[href]");
        assertNotNull(linkElement);
        assertEquals(baseUri + "/index.html", linkElement.attr("abs:href"));
    }

    @Test
    public void testCleanWithDocumentOutputSettings() {
        String bodyHtml = "<html><head><script>alert('Hello World');</script></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, outputSettings);
        assertTrue(cleanHtml.contains("\n"));
        Document cleanedDocument = Jsoup.parse(cleanHtml, baseUri);
        assertNull(cleanedDocument.select("script").first());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<input type=\"text\" name=\"username\">";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidWithInputElement() {
        String bodyHtml = "<input type=\"text\" name=\"username\" value=\"admin\">";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}