package org.jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello jsoup!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello jsoup!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(html, baseUri, Parser.xmlParser());
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello jsoup!</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertEquals(html, document.html());
    }

    @Test
    public void testConnect() throws IOException {
        String url = "http://www.example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        assertEquals(url, connection.request().url().toString());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    @Test
    public void testParseFromFileWithoutCharset() throws IOException {
        File file = new File("example.html");
        String charsetName = null;
        Document document = Jsoup.parse(file, charsetName);
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(in, charsetName, baseUri);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseFromInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parse(in, charsetName, baseUri, Parser.xmlParser());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello jsoup!</h1>";
        String baseUri = "http://www.example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello jsoup!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://www.example.com");
        int timeoutMillis = 1000;
        Document document = Jsoup.parse(url, timeoutMillis);
        assertEquals(url.toString(), document.baseUri());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello jsoup!</h1>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.none();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Hello jsoup!</h1>";
        Whitelist whitelist = Whitelist.none();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Hello jsoup!</h1>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.none();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello jsoup!</h1>";
        Whitelist whitelist = Whitelist.none();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertEquals(true, isValid);
    }
}