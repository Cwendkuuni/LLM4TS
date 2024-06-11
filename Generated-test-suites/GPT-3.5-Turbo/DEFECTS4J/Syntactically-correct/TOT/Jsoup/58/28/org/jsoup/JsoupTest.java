import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    private static final String HTML = "<html><head></head><body><h1>Hello World</h1></body></html>";

    private static final String BASE_URI = "https://example.com/";

    private static final Whitelist WHITELIST = Whitelist.basic();

    private static Document document;

    @BeforeClass
    public static void setUp() {
        document = Jsoup.parse(HTML);
    }

    @Test
    public void testParse() {
        Document parsedDocument = Jsoup.parse(HTML, BASE_URI);
        Assert.assertEquals("Parsed document should match expected document", document, parsedDocument);
    }

    @Test
    public void testParseWithParser() {
        Parser parser = Parser.htmlParser();
        Document parsedDocument = Jsoup.parse(HTML, BASE_URI, parser);
        Assert.assertEquals("Parsed document with parser should match expected document", document, parsedDocument);
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document parsedDocument = Jsoup.parse(HTML);
        Assert.assertEquals("Parsed document without base URI should match expected document", document, parsedDocument);
    }

    @Test
    public void testConnect() {
        String url = "https://example.com";
        Connection connection = Jsoup.connect(url);
        Assert.assertNotNull("Connection should not be null", connection);
        Assert.assertEquals("Connection URL should match specified URL", url, connection.url());
    }

    @Test
    public void testParseFromFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document parsedDocument = Jsoup.parse(file, charsetName, BASE_URI);
        Assert.assertEquals("Parsed document from file with charset name and base URI should match expected document", document, parsedDocument);
    }

    @Test
    public void testParseFromFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document parsedDocument = Jsoup.parse(file, charsetName);
        Assert.assertEquals("Parsed document from file with charset name should match expected document", document, parsedDocument);
    }

    @Test
    public void testParseFromInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        Document parsedDocument = Jsoup.parse(inputStream, charsetName, BASE_URI);
        Assert.assertEquals("Parsed document from input stream with charset name and base URI should match expected document", document, parsedDocument);
    }

    @Test
    public void testParseFromInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        Document parsedDocument = Jsoup.parse(inputStream, charsetName);
        Assert.assertEquals("Parsed document from input stream with charset name should match expected document", document, parsedDocument);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document parsedDocument = Jsoup.parseBodyFragment(bodyHtml, BASE_URI);
        Assert.assertEquals("Parsed body fragment with base URI should match expected document", document.body(), parsedDocument.body());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document parsedDocument = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Parsed body fragment should match expected document", document.body(), parsedDocument.body());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/");
        int timeoutMillis = 5000;
        Document parsedDocument = Jsoup.parse(url, timeoutMillis);
        Assert.assertEquals("Parsed document from URL should match expected document", document, parsedDocument);
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Hello World</h1><script>alert('xss');</script>";
        String expectedOutput = "<h1>Hello World</h1>";
        String cleanedHtml = Jsoup.clean(bodyHtml, BASE_URI, WHITELIST);
        Assert.assertEquals("Cleaned HTML with base URI and whitelist should match expected output", expectedOutput, cleanedHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1>Hello World</h1><script>alert('xss');</script>";
        String expectedOutput = "<h1>Hello World</h1>";
        String cleanedHtml = Jsoup.clean(bodyHtml, WHITELIST);
        Assert.assertEquals("Cleaned HTML with whitelist should match expected output", expectedOutput, cleanedHtml);
    }

    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Hello World</h1><script>alert('xss');</script>";
        String expectedOutput = "<h1>Hello World</h1>";
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanedHtml = Jsoup.clean(bodyHtml, BASE_URI, WHITELIST, outputSettings);
        Assert.assertEquals("Cleaned HTML with base URI, whitelist, and output settings should match expected output", expectedOutput, cleanedHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<h1>Hello World</h1>";
        boolean isValid = Jsoup.isValid(bodyHtml, WHITELIST);
        Assert.assertTrue("HTML with valid tags and attributes should be considered valid", isValid);
    }
}