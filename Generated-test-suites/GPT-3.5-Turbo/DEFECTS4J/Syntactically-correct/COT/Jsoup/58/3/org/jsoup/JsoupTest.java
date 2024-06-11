import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals(document.title(), "Hello, World!");
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Assert.assertEquals(document.title(), "Hello, World!");
    }

    @Test
    public void testConnect() {
        String url = "https://example.com";
        Connection connection = Jsoup.connect(url);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("example.html");
        Document document = Jsoup.parse(file, "UTF-8");
        Assert.assertEquals(document.title(), "Example");
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("example.html");
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(file, "UTF-8", baseUri);
        Assert.assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = DataUtil.loadStream(new URL("https://example.com"));
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        Assert.assertEquals(document.title(), "Example");
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = DataUtil.loadStream(new URL("https://example.com"));
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(inputStream, "UTF-8", "", parser);
        Assert.assertEquals(document.title(), "Example");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals(document.body().html(), bodyHtml);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1><script>alert('XSS');</script></h1>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals(cleanHtml, "<h1></h1>");
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1><script>alert('XSS');</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(valid);
    }
}