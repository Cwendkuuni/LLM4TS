package org.jsoup.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * JUnit test cases for the Jsoup class.
 */
public class JsoupTest {

    /**
     * Test parsing HTML from a string with a base URI.
     */
    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head><base href='http://example.com'></base></head><body><a href='/test'>Test</a></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        String expectedLinkHref = "/test";
        String actualLinkHref = document.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
    }

    /**
     * Test parsing HTML from a string with no base URI.
     */
    @Test
    public void testParseHtmlWithNoBaseUri() {
        String html = "<html><head><base href='http://example.com'></base></head><body><a href='/test'>Test</a></body></html>";
        Document document = Jsoup.parse(html);
        String expectedLinkHref = "http://example.com/test";
        String actualLinkHref = document.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
    }

    /**
     * Test parsing HTML from a string using a custom parser.
     */
    @Test
    public void testParseHtmlWithCustomParser() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Element p = document.selectFirst("p");
        String expectedText = "Test";
        String actualText = p.text();
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Test connecting to a URL and parsing the resulting HTML.
     */
    @Test
    public void testConnectAndParse() {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        String expectedTitle = "Example Domain";
        String actualTitle = document.title();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    /**
     * Test parsing a file as HTML.
     */
    @Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(new File(fileName), charsetName);
        String expectedTitle = "Test Title";
        String actualTitle = document.title();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    /**
     * Test parsing a file as HTML with a base URI.
     */
    @Test
    public void testParseFileWithBaseUri() throws IOException {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(fileName), charsetName, baseUri);
        String expectedLinkHref = "/test";
        String actualLinkHref = document.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
    }

    /**
     * Test parsing an input stream as HTML.
     */
    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamData = "Test";
        InputStream inputStream = new FileInputStream("test.txt");
        Document document = Jsoup.parse(inputStream, charsetName);
        String expectedText = inputStreamData;
        String actualText = document.text();
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Test parsing an input stream as HTML with a base URI.
     */
    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamData = "<html><body><a href='/test'>Test</a></body></html>";
        InputStream inputStream = new FileInputStream("test.txt");
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        String expectedLinkHref = "/test";
        String actualLinkHref = document.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
    }

    /**
     * Test parsing a file as HTML with a custom parser and a base URI.
     */
    @Test
    public void testParseFileWithCustomParserAndBaseUri() throws IOException {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(new File(fileName), charsetName, baseUri, parser);
        Element p = document.selectFirst("p");
        String expectedText = "Test";
        String actualText = p.text();
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Test parsing a body fragment.
     */
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Element p = document.selectFirst("p");
        String expectedText = "Test";
        String actualText = p.text();
        Assert.assertEquals(expectedText, actualText);
    }

    /**
     * Test fetching a URL and parsing the resulting HTML.
     */
    @Test
    public void testFetchAndParseUrl() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        String expectedTitle = "Example Domain";
        String actualTitle = document.title();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    /**
     * Test cleaning HTML with a white-list.
     */
    @Test
    public void testCleanHtml() {
        String bodyHtml = "<html><body><script>alert('Test');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        String expectedHtml = "<html><body></body></html>";
        String actualHtml = cleanedHtml;
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    /**
     * Test cleaning HTML with a base URI.
     */
    @Test
    public void testCleanHtmlWithBaseUri() {
        String bodyHtml = "<html><head><base href='http://example.com'></base></head><body><a href='/test'>Test</a></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("a");
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        String expectedLinkHref = "/test";
        String actualLinkHref = cleanedHtml.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
    }

    /**
     * Test cleaning HTML with a custom parser.
     */
    @Test
    public void testCleanHtmlWithCustomParser() {
        String bodyHtml = "<html><head><script>alert('Test');</script></head><body></body></html>";
        Parser parser = Parser.xmlParser();
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist, parser);
        String expectedHtml = "<html><head></head><body></body></html>";
        String actualHtml = cleanedHtml;
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    /**
     * Test cleaning HTML with a base URI and a custom parser.
     */
    @Test
    public void testCleanHtmlWithBaseUriAndCustomParser() {
        String bodyHtml = "<html><head><base href='http://example.com'></base></head><body><a href='/test'>Test</a></body></html>";
        Parser parser = Parser.xmlParser();
        Whitelist whitelist = Whitelist.simpleTagNames("a");
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist, parser);
        String expectedLinkHref = "/test";
        String actualLinkHref = cleanedHtml.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
    }

    /**
     * Test cleaning HTML with a white-list and output settings.
     */
    @Test
    public void testCleanHtmlWithWhiteListAndOutputSettings() {
        String bodyHtml = "<html><head><base href='http://example.com'></base></head><body><a href='/test'>Test</a></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("a");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist, outputSettings);
        String expectedLinkHref = "/test";
        String actualLinkHref = cleanedHtml.selectFirst("a").attr("href");
        Assert.assertEquals(expectedLinkHref, actualLinkHref);
        String expectedOutput = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"/test\">Test</a></body></html>";
        String actualOutput = cleanedHtml.html();
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}