import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithBaseUriAndParser() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals("", document.baseUri());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharsetName() {
        File file = new File("invalidFile.html");
        String charsetName = "invalidCharset";
        Jsoup.parse(file, charsetName, "");
    }

    @Test
    public void testParseInputStreamWithBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals(1, document.body().childNodeSize());
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src=\"image.jpg\" alt=\"Test\">";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertFalse(cleanHtml.contains("src"));
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<img src=\"image.jpg\" alt=\"Test\">";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertFalse(cleanHtml.contains("src"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<input type=\"text\" name=\"username\">";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidInput() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }
}