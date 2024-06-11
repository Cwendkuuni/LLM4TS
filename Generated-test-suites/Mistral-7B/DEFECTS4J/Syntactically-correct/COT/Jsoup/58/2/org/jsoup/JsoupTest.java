import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello", document.select("h1").text());
    }

    @Test
    public void testParseStringWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals("", document.title()); // Since no base URI is provided, no base tag is assumed
        Assert.assertEquals("Hello", document.select("h1").text());
    }

    @Test
    public void testParseStringWithCustomParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello", document.select("h1").text());
    }

    @Test
    public void testConnectToUrl() {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testConnectToInvalidUrl() {
        String url = "invalid://example.com";
        Document document = Jsoup.connect(url).get();
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document;
        try {
            document = Jsoup.parse(new File(fileName), charsetName, baseUri);
            Assert.assertEquals("Test", document.title());
            Assert.assertEquals("Hello", document.select("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("File not found or read error.");
        }
    }

    @Test
    public void testParseFileWithCharset() {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        Document document;
        try {
            document = Jsoup.parse(new File(fileName), charsetName);
            Assert.assertEquals("Test", document.title());
            Assert.assertEquals("Hello", document.select("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("File not found or read error.");
        }
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document;
        try {
            InputStream inputStream = new FileInputStream(new File(fileName));
            document = Jsoup.parse(inputStream, charsetName, baseUri);
            Assert.assertEquals("Test", document.title());
            Assert.assertEquals("Hello", document.select("h1").text());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("File not found or read error.");
        }
    }

    @Test
    public void testParseInputStreamWithCharset() {
        String fileName = "test.html";
        String charsetName = "UTF-8";
        Document document;
        try {
            InputStream inputStream = new FileInputStream(new File(fileName));
            document = Jsoup.parse(inputStream, charsetName);
            Assert.assertEquals("Test", document.title());
            Assert.assertEquals("Hello", document.select("h1").text());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("File not found or read error.");
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello", document.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        Assert.assertEquals("Hello", document.select("h1").text());
    }

    @Test(expected = IOException.class)
    public void testParseUrlWithInvalidUrl() {
        String url = "invalid://example.com";
        Document document = Jsoup.parse(url);
    }

    @Test
    public void testCleanSimpleHtml() {
        String bodyHtml = "<p>Hello <b>World!</b></p>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("Hello World!", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithBaseUri() {
        String bodyHtml = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("", cleanHtml); // Cleaner removes the img tag and its attributes
    }

    @Test
    public void testIsValidHtml() {
        String bodyHtml = "<p>Hello <b>World!</b></p>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(valid);
    }

    @Test
    public void testIsInvalidHtml() {
        String bodyHtml = "<script>alert('Hello World!');</script>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(valid);
    }
}