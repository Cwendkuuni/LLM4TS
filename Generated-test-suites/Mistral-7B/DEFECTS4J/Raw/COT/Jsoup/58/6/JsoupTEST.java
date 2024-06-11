import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='https://example.com'></base></head><body><a href='/test.html'>Test</a></body></html>";
        Document doc = Jsoup.parse(html, "https://example.com");
        Assert.assertEquals("https://example.com/test.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseWithCustomParser() {
        String html = "<html><body><p>Test</p></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Test", doc.select("p").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><base href='/'></base></head><body><a href='test.html'>Test</a></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("/test.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testConnect() throws Exception {
        Connection connection = Jsoup.connect("http://example.com");
        Document doc = connection.get();
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Test", doc.select("p").text());
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com");
        Document doc = Jsoup.parse(url, 5000);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidUrl() throws IOException {
        Jsoup.parse("data:text/html,<html><body><p>Test</p></body></html>", 5000);
    }

    @Test
    public void testClean() {
        String html = "<html><head><script src='xss.js'></script></head><body><div id='xss'><script>alert('XSS')</script></div></body></html>";
        String cleanHtml = Jsoup.clean(html, Whitelist.DEFAULT);
        Assert.assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testCleanWithCustomWhitelist() {
        String html = "<html><head><script src='xss.js'></script></head><body><div id='xss'><script>alert('XSS')</script></div></body></html>";
        Whitelist customWhitelist = new Whitelist().addTags("div");
        String cleanHtml = Jsoup.clean(html, customWhitelist);
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertTrue(cleanHtml.contains("<div"));
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Assert.assertFalse(Jsoup.isValid(html, Whitelist.DEFAULT));
        Assert.assertFalse(Jsoup.isValid("<script>alert('XSS')</script>", Whitelist.DEFAULT));
    }

    @Test
    public void testIsValidWithCustomWhitelist() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist customWhitelist = new Whitelist().addTags("script");
        Assert.assertFalse(Jsoup.isValid(html, customWhitelist));
        Assert.assertFalse(Jsoup.isValid("<script>alert('XSS')</script>", customWhitelist));

        html = "<html><body><div id='xss'><script>alert('XSS')</script></div></body></html>";
        customWhitelist = new Whitelist().addTags("div");
        Assert.assertTrue(Jsoup.isValid(html, customWhitelist));
        Assert.assertTrue(Jsoup.isValid("<div id='xss'><script>alert('XSS')</script></div>", customWhitelist));
    }
}