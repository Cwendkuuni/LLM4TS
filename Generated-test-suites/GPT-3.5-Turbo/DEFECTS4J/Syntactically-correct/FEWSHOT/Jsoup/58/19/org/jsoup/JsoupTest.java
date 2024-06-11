package org.jsoup;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {
    private static final String HTML = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1><p>This is a test document.</p></body></html>";
    private static final String BASE_URI = "https://example.com";
    private static final String BODY_HTML = "<h1>Hello World!</h1><p>This is a test document.</p>";

    private Document document;

    @Before
    public void setUp() {
        document = Jsoup.parse(HTML);
    }

    @Test
    public void testParse() {
        Document parsedDocument = Jsoup.parse(HTML, BASE_URI);

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testParseWithParser() {
        Parser parser = Parser.htmlParser();
        Document parsedDocument = Jsoup.parse(HTML, BASE_URI, parser);

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document parsedDocument = Jsoup.parse(HTML);

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://example.com");

        assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        Document parsedDocument = Jsoup.parse(file, "UTF-8", BASE_URI);

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        Document parsedDocument = Jsoup.parse(file, "UTF-8");

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document parsedDocument = Jsoup.parse(inputStream, "UTF-8", BASE_URI);

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document parsedDocument = Jsoup.parse(inputStream, "UTF-8");

        assertNotNull(parsedDocument);
        assertEquals(HTML, parsedDocument.html());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document parsedDocument = Jsoup.parseBodyFragment(BODY_HTML, BASE_URI);

        assertNotNull(parsedDocument);
        assertEquals(BODY_HTML, parsedDocument.html());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        Document parsedDocument = Jsoup.parseBodyFragment(BODY_HTML);

        assertNotNull(parsedDocument);
        assertEquals(BODY_HTML, parsedDocument.html());
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(HTML, BASE_URI, whitelist);

        assertNotNull(cleanedHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(HTML, whitelist);

        assertNotNull(cleanedHtml);
    }

    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanedHtml = Jsoup.clean(HTML, BASE_URI, whitelist, outputSettings);

        assertNotNull(cleanedHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(HTML, whitelist);

        assertTrue(isValid);
    }
}