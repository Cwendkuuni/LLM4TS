import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(html, baseUri);

        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());

        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";

        Document document = Jsoup.parse(html);

        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);

        Assert.assertEquals(url, connection.url());
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(file, charsetName, baseUri);

        Assert.assertTrue(file.exists());
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";

        Document document = Jsoup.parse(file, charsetName);

        Assert.assertTrue(file.exists());
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(inputStream, charsetName, baseUri);

        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFragmentWithBaseUri() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";

        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testParseFragmentWithoutBaseUri() {
        String bodyHtml = "<p>Hello World!</p>";

        Document document = Jsoup.parseBodyFragment(bodyHtml);

        Assert.assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testParseURLWithTimeout() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 10000;

        Document document = Jsoup.parse(url, timeoutMillis);

        Assert.assertNotNull(document);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        Assert.assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        Assert.assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        Assert.assertEquals(bodyHtml, cleanHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();

        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        Assert.assertTrue(isValid);
    }
}