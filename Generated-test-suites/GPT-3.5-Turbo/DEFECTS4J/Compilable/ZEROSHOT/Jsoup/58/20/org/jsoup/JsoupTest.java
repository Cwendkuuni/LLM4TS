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
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    // Test case for parse(String html, String baseUri) method
    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(html, baseUri);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parse(String html, String baseUri, Parser parser) method
    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = Parser.xmlParser();

        Document doc = Jsoup.parse(html, baseUri, parser);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parse(String html) method
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";

        Document doc = Jsoup.parse(html);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for connect(String url) method
    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";

        Connection conn = Jsoup.connect(url);

        assertNotNull(conn);
    }

    // Test case for parse(File in, String charsetName, String baseUri) method
    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(file, charsetName, baseUri);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parse(File in, String charsetName) method
    @Test
    public void testParseFileWithCharset() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";

        Document doc = Jsoup.parse(file, charsetName);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(inputStream, charsetName, baseUri);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseInputStreamWithCharsetAndParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document doc = Jsoup.parse(inputStream, charsetName, baseUri, parser);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parseBodyFragment(String bodyHtml, String baseUri) method
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parseBodyFragment(String bodyHtml) method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";

        Document doc = Jsoup.parseBodyFragment(bodyHtml);

        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // Test case for parse(URL url, int timeoutMillis) method
    @Test
    public void testParseWithTimeout() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;

        Document doc = Jsoup.parse(url, timeoutMillis);

        assertTrue(doc != null);
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist) method
    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Hello, <b>World!</b></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertEquals("<h1>Hello, World!</h1>", cleanHtml);
    }

    // Test case for clean(String bodyHtml, Whitelist whitelist) method
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1>Hello, <b>World!</b></h1>";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        assertEquals("<h1>Hello, World!</h1>", cleanHtml);
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist, Document.OutputSettings outputSettings) method
    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Hello, <b>World!</b></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        assertEquals("<h1>Hello, World!</h1>", cleanHtml);
    }

    // Test case for isValid(String bodyHtml, Whitelist whitelist) method
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<h1>Hello, <b>World!</b></h1>";
        Whitelist whitelist = Whitelist.basic();

        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        assertTrue(isValid);
    }
  
}