package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    private Document document;
    private Document cleanDocument;
    
    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, Jsoup!</h1></body></html>";
        document = Jsoup.parse(html);
        cleanDocument = Jsoup.parseBodyFragment(html);
    }

    @Test
    public void parseWithBaseUri_shouldReturnDocumentWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, Jsoup!</h1></body></html>";
        String baseUri = "http://example.com";
        Document result = Jsoup.parse(html, baseUri);
        assertEquals(baseUri, result.baseUri());
    }

    @Test
    public void parseWithParser_shouldReturnDocumentParsedByGivenParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, Jsoup!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document result = Jsoup.parse(html, baseUri, parser);
        assertEquals(parser, result.parser());
    }

    @Test
    public void parseWithoutBaseUri_shouldReturnDocumentWithEmptyBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, Jsoup!</h1></body></html>";
        Document result = Jsoup.parse(html);
        assertEquals("", result.baseUri());
    }

    @Test
    public void connect_shouldReturnConnectionToGivenUrl() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertEquals(url, connection.url());
    }

    @Test
    public void parseFromFile_shouldReturnDocumentFromFile() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File(filePath);
        Document result = Jsoup.parse(file, charsetName, baseUri);
        assertEquals(file.getAbsolutePath(), result.baseUri());
    }

    @Test
    public void parseFromInputStream_shouldReturnDocumentFromInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, Jsoup!</h1></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document result = Jsoup.parse(inputStream, charsetName, baseUri);
        assertEquals(baseUri, result.baseUri());
    }

    @Test
    public void parseBodyFragmentWithBaseUri_shouldReturnDocumentWithBaseUri() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        String baseUri = "http://example.com";
        Document result = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(baseUri, result.baseUri());
    }

    @Test
    public void parseBodyFragmentWithoutBaseUri_shouldReturnDocumentWithEmptyBaseUri() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        Document result = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("", result.baseUri());
    }

    @Test
    public void cleanWithWhitelist_shouldReturnSafeHtml() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals(bodyHtml, result);
    }

    @Test
    public void cleanWithoutBaseUri_shouldReturnSafeHtmlWithEmptyBaseUri() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        Whitelist whitelist = Whitelist.basic();
        String result = Jsoup.clean(bodyHtml, whitelist);
        assertEquals(bodyHtml, result);
    }

    @Test
    public void cleanWithOutputSettings_shouldReturnSafeHtmlWithOutputSettings() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals(bodyHtml, result);
    }

    @Test
    public void isValid_shouldReturnTrueIfNoTagsOrAttributesRemoved() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean result = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(result);
    }

}