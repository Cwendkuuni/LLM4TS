import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello, Jsoup!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertEquals("Hello, Jsoup!", doc.select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello, Jsoup!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        Assert.assertEquals("Hello, Jsoup!", doc.select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello, Jsoup!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Hello, Jsoup!", doc.select("h1").text());
    }

    @Test
    public void testConnect() throws IOException {
        String url = "https://www.example.com";
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();
        Assert.assertTrue(doc.title().startsWith("Example Domain"));
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream in = getClass().getResourceAsStream("file.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream in = getClass().getResourceAsStream("file.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(in, charsetName);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello, Jsoup!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello, Jsoup!</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello, Jsoup!", doc.select("h1").text());
    }

    @Test
    public void testParseUrlWithTimeoutMillis() throws IOException {
        URL url = new URL("https://www.example.com");
        int timeoutMillis = 5000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        Assert.assertTrue(doc.title().startsWith("Example Domain"));
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<script>alert('XSS');</script>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUriAndWhitelist() {
        String bodyHtml = "<script>alert('XSS');</script>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<script>alert('XSS');</script>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testIsValidWithBodyHtmlAndWhitelist() {
        String bodyHtml = "<p>Hello, <b>Jsoup</b>!</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }

}