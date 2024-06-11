package org.jsoup.test;

import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * JsoupTest class containing test cases for Jsoup class methods.
 */
public class JsoupTest {

    /**
     * Test case for parse method with valid HTML and baseUri.
     */
    @org.junit.Test
    public void testParseValidHtmlWithBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
        Assert.assertEquals("html", document.tagName());
    }

    /**
     * Test case for parse method with valid HTML and no baseUri.
     */
    @org.junit.Test
    public void testParseValidHtmlWithNoBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
        Assert.assertEquals("html", document.tagName());
    }

    /**
     * Test case for parse method with invalid HTML and baseUri.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtmlWithBaseUri() {
        String html = "<html><body></body>";
        String baseUri = "http://example.com";
        Jsoup.parse(html, baseUri);
    }

    /**
     * Test case for parse method with invalid HTML and no baseUri.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtmlWithNoBaseUri() {
        String html = "<html><body></body>";
        Jsoup.parse(html);
    }

    /**
     * Test case for parseBodyFragment method with valid HTML body fragment and baseUri.
     */
    @org.junit.Test
    public void testParseBodyFragmentValidHtmlWithBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
        Assert.assertEquals("body", document.tagName());
    }

    /**
     * Test case for parseBodyFragment method with valid HTML body fragment and no baseUri.
     */
    @org.junit.Test
    public void testParseBodyFragmentValidHtmlWithNoBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
        Assert.assertEquals("body", document.tagName());
    }

    /**
     * Test case for connect method with valid URL.
     */
    @org.junit.Test
    public void testConnectValidUrl() throws IOException {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        Assert.assertNotNull(document);
        Assert.assertEquals("Example Domain", document.title());
        Assert.assertEquals("html", document.tagName());
    }

    /**
     * Test case for parse method with file and valid charsetName.
     */
    @org.junit.Test
    public void testParseFileValidCharsetName() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "/path/to/file.html";
        Document document = Jsoup.parse(new File(filePath), charsetName);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test File", document.title());
        Assert.assertEquals("html", document.tagName());
    }

    /**
     * Test case for parse method with file and null charsetName.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseFileNullCharsetName() throws IOException {
        String filePath = "/path/to/file.html";
        Document document = Jsoup.parse(new File(filePath), null);
    }

    /**
     * Test case for parse method with input stream and valid charsetName.
     */
    @org.junit.Test
    public void testParseInputStreamValidCharsetName() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(inputStreamContent.getBytes());
        Document document = Jsoup.parse(inputStream, charsetName);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("html", document.tagName());
    }

    /**
     * Test case for parse method with input stream and null charsetName.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamNullCharsetName() throws IOException {
        String inputStreamContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(inputStreamContent.getBytes());
        Document document = Jsoup.parse(inputStream, null);
    }

    /**
     * Test case for clean method with valid bodyHtml and whitelist.
     */
    @org.junit.Test
    public void testCleanValidBodyHtmlWithWhitelist() {
        String bodyHtml = "<div id='test' class='test'><p>Test</p></div>";
        Whitelist whitelist = Whitelist.relaxed();
        String expectedOutput = "<div id='test' class='test'><p>Test</p></div>";
        String actualOutput = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test case for clean method with valid bodyHtml and empty whitelist.
     */
    @org.junit.Test
    public void testCleanValidBodyHtmlWithEmptyWhitelist() {
        String bodyHtml = "<div id='test' class='test'><p>Test</p></div>";
        Whitelist whitelist = Whitelist.empty();
        String expectedOutput = "";
        String actualOutput = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test case for clean method with invalid bodyHtml and whitelist.
     */
    @org.junit.Test
    public void testCleanInvalidBodyHtmlWithWhitelist() {
        String bodyHtml = "<script>alert('Test');</script>";
        Whitelist whitelist = Whitelist.relaxed();
        String expectedOutput = "";
        String actualOutput = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}