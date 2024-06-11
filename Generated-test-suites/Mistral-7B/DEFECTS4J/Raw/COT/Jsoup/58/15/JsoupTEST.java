package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    // Basic test cases for parse methods

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseString() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseStringWithParserAndEmptyBaseUri() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, parser, "");
        Assert.assertNotNull(document);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtmlString() {
        String html = null;
        Jsoup.parse(html);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtmlString() {
        String html = "";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        String baseUri = null;
        Document document = Jsoup.parse(html, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullParser() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Parser parser = null;
        Document document = Jsoup.parse(html, "", parser);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() {
        File file = new File("non_existent_file.html");
        Document document = null;
        try {
            document = Jsoup.parse(file, "UTF-8", "");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInput() {
        InputStream inputStream = null;
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "UTF-8", "");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "UTF-8", "");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes("InvalidCharset"));
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "InvalidCharset", "");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamWithNullBaseUri() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes("UTF-8"));
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "UTF-8", null);
        } catch (IOException e) {
            // Ignore
        }
        Assert.assertNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamWithNullParser() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes("UTF-8"));
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "UTF-8", null);
        } catch (IOException e) {
            // Ignore
        }
        Assert.assertNull(document);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNull(cleanedBodyHtml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullBaseUri() {
        String bodyHtml = "<script>alert('Test')</script>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, null, whitelist);
        Assert.assertNull(cleanedBodyHtml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullWhitelist() {
        String bodyHtml = "<script>alert('Test')</script>";
        String baseUri = "";
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, baseUri, null);
        Assert.assertNull(cleanedBodyHtml);
    }

    @Test
    public void testCleanEmptyBodyHtml() {
        String bodyHtml = "";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("", cleanedBodyHtml);
    }

    @Test
    public void testCleanBodyHtmlWithAllowedTags() {
        String bodyHtml = "<p><a href=\"http://example.com\">Link</a></p>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("<p><a href=\"http://example.com\">Link</a></p>", cleanedBodyHtml);
    }

    @Test
    public void testCleanBodyHtmlWithDisallowedTags() {
        String bodyHtml = "<script>alert('Test')</script>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("", cleanedBodyHtml);
    }

    @Test
    public void testCleanBodyHtmlWithScriptTagAsAttributeValue() {
        String bodyHtml = "<p id=\"test\" onclick=\"alert('Test')\"></p>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedBodyHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("<p id=\"test\"></p>", cleanedBodyHtml);
    }
}