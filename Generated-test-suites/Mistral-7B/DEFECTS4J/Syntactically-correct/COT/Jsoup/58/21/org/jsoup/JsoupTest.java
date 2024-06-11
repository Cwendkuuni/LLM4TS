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

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><a href=\"/about\">About</a></body></html>";
        String baseUri = "https://example.com";
        Document actual = Jsoup.parse(html, baseUri);

        Document expected = Jsoup.parse("<html><body><a href=\"https://example.com/about\">About</a></body></html>");
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseWithCustomParser() {
        String html = "<html><body></body></html>";
        String baseUri = "";
        Parser parser = Parser.xmlParser();
        Document actual = Jsoup.parse(html, baseUri, parser);

        Document expected = parser.parseInput(html, baseUri);
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><a href=\"/about\">About</a></body></html>";
        Document actual = Jsoup.parse(html);

        Document expected = Jsoup.parse("<html><body><a href=\"about\">About</a></body></html>");
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testConnectToUrl() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);

        assertEquals(url, connection.url());
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        String html = DataUtil.load(file, charsetName, baseUri);

        Document actual = Jsoup.parse(html, charsetName, baseUri);
        Document expected = Jsoup.parse(html);
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html");

        Document actual = Jsoup.parse(file, charsetName);
        Document expected = Jsoup.parse(DataUtil.load(file, charsetName));
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        String html = DataUtil.load(inputStream, charsetName, baseUri);

        Document actual = Jsoup.parse(inputStream, charsetName, baseUri);
        Document expected = Jsoup.parse(html);
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);

        Document actual = Jsoup.parse(inputStream, charsetName);
        Document expected = Jsoup.parse(DataUtil.load(file, charsetName));
        assertEquals(expected.html(), actual.html());
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        File file = new File("test.html");
        Jsoup.parse(file, charsetName);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Jsoup.parse(inputStream, charsetName);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Test</div>";
        String baseUri = "";
        Document actual = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        Document expected = Jsoup.parseBodyFragment("<div>Test</div>");
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<div>Test</div>";
        Document actual = Jsoup.parseBodyFragment(bodyHtml);

        Document expected = Jsoup.parseBodyFragment("<div>Test</div>");
        assertEquals(expected.html(), actual.html());
    }

    @Test
    public void testParseWithInvalidUrl() {
        String url = "invalid:url";
        Exception exception = assertThrows(MalformedURLException.class, () -> Jsoup.parse(url));
        String expectedMessage = "Invalid URL: invalid:url";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test(expected = HttpStatusException.class)
    public void testParseWithInvalidHttpResponse() throws IOException {
        String url = "http://example.com/invalid_status";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
    }

    @Test(expected = UnsupportedMimeTypeException.class)
    public void testParseWithUnsupportedMimeType() throws IOException {
        String url = "http://example.com";
        connection.userAgent("Mozilla/5.0");
        connection.header("Accept", "application/xml");
        Document document = connection.get();
    }

    @Test
    public void testCleanWithValidInput() {
        String bodyHtml = "<div id=\"test\" class=\"valid\">Test</div>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        String actual = Jsoup.clean(bodyHtml, baseUri, whitelist);

        String expected = "<div id=\"test\" class=\"valid\">Test</div>";
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanWithInvalidInput() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        String actual = Jsoup.clean(bodyHtml, baseUri, whitelist);

        String expected = ""
                + "<div>"
                + "</div>";
        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidWithValidInput() {
        String bodyHtml = "<div id=\"test\" class=\"valid\">Test</div>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean actual = Jsoup.isValid(bodyHtml, whitelist);

        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidWithInvalidInput() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean actual = Jsoup.isValid(bodyHtml, whitelist);

        boolean expected = false;
        assertEquals(expected, actual);
    }
}