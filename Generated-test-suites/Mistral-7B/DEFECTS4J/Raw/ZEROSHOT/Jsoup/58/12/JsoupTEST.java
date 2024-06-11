package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * JsoupTest class to test all methods in Jsoup class
 */
public class JsoupTest {

    @Test
    public void testParseString() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
    }

    @Test
    public void testParseStringWithBaseURI() throws IOException {
        String html = "<img src='image.jpg' />";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Element img = document.select("img").first();
        String src = img.attr("src");
        String expected = "http://example.com/image.jpg";
        assertNotNull(img);
        assertNotNull(src);
        assertEquals(expected, src);
    }

    @Test
    public void testParseStringWithParser() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseStringEmpty() throws IOException {
        Document document = Jsoup.parse("");
        assertNotNull(document);
    }

    @Test
    public void testConnectUrl() throws IOException {
        String url = "http://jsoup.org";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        assertNotNull(document);
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(new File(filePath), charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseFileWithBaseURI() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(filePath), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String filePath = "test.html";
        InputStream inputStream = new FileInputStream(filePath);
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputStream, charsetName);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBaseURI() throws IOException {
        String filePath = "test.html";
        InputStream inputStream = new FileInputStream(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<p>Test</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentWithBaseURI() throws IOException {
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://jsoup.org";
        Document document = Jsoup.parse(new URL(url));
        assertNotNull(document);
    }

    @Test
    public void testClean() throws IOException {
        String bodyHtml = "<html><body><p>Test <script>alert(1)</script></p></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Test</p>", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() throws IOException {
        String bodyHtml = "<html><body><p>Test <script>alert(1)</script></p></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Test</p>", cleanHtml);
    }

    @Test
    public void testIsValid() throws IOException {
        String bodyHtml = "<html><body><p>Test <script>alert(1)</script></p></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithWhitelist() throws IOException {
        String bodyHtml = "<html><body><p>Test <script>alert(1)</script></p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}