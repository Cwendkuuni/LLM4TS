package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * JUnit test class for the Jsoup class
 */
public class JsoupTest {

    /**
     * Test parse method with valid HTML and base URI
     */
    @Test
    public void testParseWithValidHtmlAndBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World", document.body().text());
    }

    /**
     * Test parse method with valid HTML and no base URI
     */
    @Test
    public void testParseWithValidHtmlAndNoBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.baseUri());
        Assert.assertEquals("Test", document.title().text());
        Assert.assertEquals("Hello World", document.body().text());
    }

    /**
     * Test parse method with invalid HTML
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidHtml() {
        String html = "<html>";
        Jsoup.parse(html);
    }

    /**
     * Test parse method with null input
     */
    @Test(expected = NullPointerException.class)
    public void testParseWithNullInput() {
        Jsoup.parse(null);
    }

    /**
     * Test parse method using custom parser
     */
    @Test
    public void testParseWithCustomParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title().text());
        Assert.assertEquals("Hello World", document.body().text());
    }

    /**
     * Test connect method
     */
    @Test
    public void testConnect() throws IOException {
        String url = "https://example.com";
        Document document = Jsoup.connect(url).get();
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
        Assert.assertNotNull(document.body());
    }

    /**
     * Test parse method with file
     */
    @Test
    public void testParseWithFile() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document document = Jsoup.parse(file, "UTF-8");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
        Assert.assertNotNull(document.body());
        Assert.assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    /**
     * Test parse method with invalid charset
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidCharset() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document document = Jsoup.parse(file, "invalid-charset");
    }

    /**
     * Test parse method with input stream
     */
    @Test
    public void testParseWithInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        byte[] htmlBytes = html.getBytes();
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        Document document = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title().text());
        Assert.assertEquals("Hello World", document.body().text());
    }

    /**
     * Test parse method with input stream and custom parser
     */
    @Test
    public void testParseWithInputStreamAndCustomParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        byte[] htmlBytes = html.getBytes();
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(inputStream, "UTF-8", parser);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.title().text());
        Assert.assertEquals("Hello World", document.body().text());
    }

    /**
     * Test parseBodyFragment method with valid HTML
     */
    @Test
    public void testParseBodyFragmentWithValidHtml() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.body().text());
    }

    /**
     * Test parseBodyFragment method with null input
     */
    @Test
    public void testParseBodyFragmentWithNullInput() {
        String bodyHtml = null;
        try {
            Jsoup.parseBodyFragment(bodyHtml);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test parse method with URL
     */
    @Test
    public void testParseWithUrl() throws IOException {
        String url = "https://example.com";
        Document document = Jsoup.parse(new URL(url), 5000);
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.title());
        Assert.assertNotNull(document.body());
    }

    /**
     * Test clean method with valid HTML and whitelist
     */
    @Test
    public void testCleanWithValidHtmlAndWhitelist() {
        String bodyHtml = "<html><body><div id='test' class='test'><a href='/test' target='_blank'>Test</a></div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        Document document = Jsoup.parse(cleanedHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals(0, document.body().childNodeSize());
    }

    /**
     * Test clean method with invalid HTML and whitelist
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCleanWithInvalidHtmlAndWhitelist() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        Whitelist whitelist = Whitelist.relaxed();
        Jsoup.clean(bodyHtml, whitelist);
    }

    /**
     * Test clean method with null input and whitelist
     */
    @Test(expected = NullPointerException.class)
    public void testCleanWithNullInputAndWhitelist() {
        String bodyHtml = null;
        Whitelist whitelist = Whitelist.relaxed();
        Jsoup.clean(bodyHtml, whitelist);
    }

    /**
     * Test clean method with valid HTML and no whitelist
     */
    @Test
    public void testCleanWithValidHtmlAndNoWhitelist() {
        String bodyHtml = "<html><body><div id='test' class='test'><a href='/test' target='_blank'>Test</a></div></body></html>";
        String cleanedHtml = Jsoup.clean(bodyHtml);
        Document document = Jsoup.parse(cleanedHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals(0, document.body().childNodeSize());
    }

    /**
     * Test clean method with valid HTML, whitelist and output settings
     */
    @Test
    public void testCleanWithValidHtmlWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><div id='test' class='test'><a href='/test' target='_blank'>Test</a></div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = Document.OutputSettings.prettyPrint();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist, outputSettings);
        Document document = Jsoup.parse(cleanedHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals(0, document.body().childNodeSize());
        Assert.assertTrue(cleanedHtml.startsWith("<html>"));
        Assert.assertTrue(cleanedHtml.endsWith("</html>"));
    }

    /**
     * Test isValid method with valid HTML and whitelist
     */
    @Test
    public void testIsValidWithValidHtmlAndWhitelist() {
        String bodyHtml = "<html><body><div id='test' class='test'><a href='/test' target='_blank'>Test</a></div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }

    /**
     * Test isValid method with invalid HTML and whitelist
     */
    @Test
    public void testIsValidWithInvalidHtmlAndWhitelist() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }

    /**
     * Test isValid method with null input and whitelist
     */
    @Test
    public void testIsValidWithNullInputAndWhitelist() {
        String bodyHtml = null;
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }
}