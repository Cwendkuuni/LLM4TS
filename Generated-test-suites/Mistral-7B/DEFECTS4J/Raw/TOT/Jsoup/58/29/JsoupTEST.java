package org.jsoup.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Jsoup test class. Contains test cases for various methods in the Jsoup class.
 */
public class JsoupTest {

    /**
     * Test for parse method with valid HTML and base URI.
     */
    @Test
    public void testParseValidHtmlWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Element titleElement = document.title();
        Assert.assertEquals("Test", titleElement.text());
    }

    /**
     * Test for parse method with valid HTML and no base URI.
     */
    @Test
    public void testParseValidHtmlWithoutBaseUri() {
        String html = "<html><head><base href=\"https://example.com\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Element titleElement = document.title();
        Assert.assertEquals("Test", titleElement.text());
    }

    /**
     * Test for parse method with invalid HTML.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
        Jsoup.parse(html);
    }

    /**
     * Test for parse method with valid HTML and custom Parser.
     */
    @Test
    public void testParseValidHtmlWithCustomParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document document = Jsoup.parse(html, "", parser);
        Element titleElement = document.title();
        Assert.assertEquals("Test", titleElement.text());
    }

    /**
     * Test for connect method with valid URL.
     */
    @Test
    public void testConnectValidUrl() throws IOException {
        Document document = Jsoup.connect("https://example.com").get();
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
    }

    /**
     * Test for parse method with file input, valid charset and base URI.
     */
    @Test
    public void testParseFileValidCharsetAndBaseUri() throws IOException {
        String filePath = "path/to/your/test.html";
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(new File(filePath), charsetName, baseUri);
        Element titleElement = document.title();
        Assert.assertEquals("Test", titleElement.text());
    }

    /**
     * Test for parse method with file input and no charset and base URI.
     */
    @Test
    public void testParseFileNoCharsetAndBaseUri() throws IOException {
        String filePath = "path/to/your/test.html";
        Document document = Jsoup.parse(new File(filePath));
        Element titleElement = document.title();
        Assert.assertEquals("Test", titleElement.text());
    }

    /**
     * Test for clean method with valid input HTML, whitelist and base URI.
     */
    @Test
    public void testCleanValidInputHtmlWithWhitelistAndBaseUri() {
        String bodyHtml = "<div id=\"myDiv\"><p>Test <a href=\"/test\">Link</a></p></div>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Document document = Jsoup.parse(cleanHtml);
        Element linkElement = document.getElementById("myDiv").getElementsByTag("a").first();
        Assert.assertEquals("/test", linkElement.attr("href"));
    }

    /**
     * Test for clean method with valid input HTML and no whitelist and base URI.
     */
    @Test
    public void testCleanValidInputHtmlNoWhitelistAndBaseUri() {
        String bodyHtml = "<div id=\"myDiv\"><p>Test <a href=\"/test\">Link</a></p></div>";
        String cleanHtml = Jsoup.clean(bodyHtml);
        Document document = Jsoup.parse(cleanHtml);
        Element linkElement = document.getElementById("myDiv").getElementsByTag("a").first();
        Assert.assertEquals("/test", linkElement.attr("href"));
    }

    /**
     * Test for clean method with invalid input HTML and whitelist and base URI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCleanInvalidInputHtmlWithWhitelistAndBaseUri() {
        String bodyHtml = "<html><body><script>alert('XSS')</script></body></html>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.simpleTags();
        Jsoup.clean(bodyHtml, baseUri, whitelist);
    }

    /**
     * Test for clean method with invalid input HTML and no whitelist and base URI.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCleanInvalidInputHtmlNoWhitelistAndBaseUri() {
        String bodyHtml = "<html><body><script>alert('XSS')</script></body></html>";
        Jsoup.clean(bodyHtml);
    }

    /**
     * Test for isValid method with valid input HTML and whitelist.
     */
    @Test
    public void testIsValidValidInputHtmlWithWhitelist() {
        String bodyHtml = "<div id=\"myDiv\"><p>Test <a href=\"/test\">Link</a></p></div>";
        Whitelist whitelist = Whitelist.simpleTags();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }

    /**
     * Test for isValid method with invalid input HTML and whitelist.
     */
    @Test
    public void testIsValidInvalidInputHtmlWithWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTags();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }

    /**
     * Test for isValid method with valid input HTML and no whitelist.
     */
    @Test
    public void testIsValidValidInputHtmlNoWhitelist() {
        String bodyHtml = "<div id=\"myDiv\"><p>Test <a href=\"/test\">Link</a></p></div>";
        boolean isValid = Jsoup.isValid(bodyHtml);
        Assert.assertTrue(isValid);
    }

    /**
     * Test for isValid method with invalid input HTML and no whitelist.
     */
    @Test
    public void testIsValidInvalidInputHtmlNoWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS')</script></body></html>";
        boolean isValid = Jsoup.isValid(bodyHtml);
        Assert.assertFalse(isValid);
    }
}