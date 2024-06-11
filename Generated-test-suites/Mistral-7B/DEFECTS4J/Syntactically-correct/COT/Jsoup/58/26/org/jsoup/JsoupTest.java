import org.junit.Test;
import org.junit.Assert;
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
    public void testParseStringWithBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseStringWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(new File(filePath), charsetName, baseUri);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(new File(filePath), charsetName);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, charsetName);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://example.com";
        Document doc = Jsoup.parse(new URL(url), 5000);
        Assert.assertEquals("", doc.title()); // blank title for an URL, no HTML provided
    }

    @Test(expected = IOException.class)
    public void testParseUrlWithMalformedUrl() {
        String url = "invalid_protocol://example.com";
        Jsoup.parse(new URL(url), 5000);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src='image.jpg' />";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, outputSettings);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><h1 id='test'></h1></body></html>";
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("h1");
        whitelist.addAttribute("h1", "id");
        Assert.assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidWithInvalidTag() {
        String bodyHtml = "<html><body><script></script></body></html>";
        Whitelist whitelist = new Whitelist();
        Assert.assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}