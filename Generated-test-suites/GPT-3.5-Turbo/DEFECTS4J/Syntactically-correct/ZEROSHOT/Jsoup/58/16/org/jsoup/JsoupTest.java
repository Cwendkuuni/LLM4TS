import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    private String testHtml;
    private String testBaseUri;
    private String testCharset;
    private String testBodyHtml;
    private String testUrl;
    private String testWhitelist;
    private int testTimeout;
    private Jsoup jsoup;

    @Before
    public void setUp() {
        testHtml = "<html><head></head><body><p>Test HTML</p></body></html>";
        testBaseUri = "http://example.com";
        testCharset = "UTF-8";
        testBodyHtml = "<p>Test Body HTML</p>";
        testUrl = "http://example.com";
        testWhitelist = "<p>Test Whitelist</p>";
        testTimeout = 5000;
        jsoup = new Jsoup();
    }

    @After
    public void tearDown() {
        testHtml = null;
        testBaseUri = null;
        testCharset = null;
        testBodyHtml = null;
        testUrl = null;
        testWhitelist = null;
        testTimeout = 0;
        jsoup = null;
    }

    @Test
    public void testParseStringString() {
        Document document = jsoup.parse(testHtml, testBaseUri);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
    }

    @Test
    public void testParseStringStringParser() {
        Parser parser = Parser.xmlParser();
        Document document = jsoup.parse(testHtml, testBaseUri, parser);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
    }

    @Test
    public void testParseString() {
        Document document = jsoup.parse(testHtml);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
    }

    @Test
    public void testConnect() {
        Connection connection = jsoup.connect(testUrl);
        assertNotNull(connection);
    }

    @Test
    public void testParseFileStringString() throws IOException {
        File file = new File("test.html");
        Document document = jsoup.parse(file, testCharset, testBaseUri);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
        file.delete();
    }

    @Test
    public void testParseFileString() throws IOException {
        File file = new File("test.html");
        Document document = jsoup.parse(file, testCharset);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
        file.delete();
    }

    @Test
    public void testParseInputStreamStringString() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = jsoup.parse(inputStream, testCharset, testBaseUri);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamStringStringParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Parser parser = Parser.xmlParser();
        Document document = jsoup.parse(inputStream, testCharset, testBaseUri, parser);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
        inputStream.close();
    }

    @Test
    public void testParseBodyFragmentStringString() {
        Document document = jsoup.parseBodyFragment(testBodyHtml, testBaseUri);
        assertNotNull(document);
        assertEquals(testBodyHtml, document.body().html());
    }

    @Test
    public void testParseBodyFragmentString() {
        Document document = jsoup.parseBodyFragment(testBodyHtml);
        assertNotNull(document);
        assertEquals(testBodyHtml, document.body().html());
    }

    @Test
    public void testParseURLInt() throws IOException {
        URL url = new URL(testUrl);
        Document document = jsoup.parse(url, testTimeout);
        assertNotNull(document);
        assertEquals(testHtml, document.html());
    }

    @Test
    public void testCleanStringStringWhitelist() {
        String cleanHtml = jsoup.clean(testBodyHtml, testBaseUri, Whitelist.basic());
        assertNotNull(cleanHtml);
        assertEquals("<p>Test Body HTML</p>", cleanHtml);
    }

    @Test
    public void testCleanStringWhitelist() {
        String cleanHtml = jsoup.clean(testBodyHtml, Whitelist.basic());
        assertNotNull(cleanHtml);
        assertEquals("<p>Test Body HTML</p>", cleanHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false).escapeMode(Document.EscapeMode.xhtml);
        String cleanHtml = jsoup.clean(testBodyHtml, testBaseUri, Whitelist.basic(), outputSettings);
        assertNotNull(cleanHtml);
        assertEquals("<p>Test Body HTML</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        boolean isValid = jsoup.isValid(testBodyHtml, Whitelist.basic());
        assertTrue(isValid);
    }
}