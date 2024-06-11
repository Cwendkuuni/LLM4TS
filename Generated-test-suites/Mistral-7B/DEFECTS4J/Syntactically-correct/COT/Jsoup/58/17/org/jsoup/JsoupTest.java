import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='https://example.com'></base></head><body><a href='about.html'>About</a></body></html>";
        Document document = Jsoup.parse(html, "https://example.com");
        Assert.assertEquals("About", document.select("a[href='about.html']").text());
    }

    @Test
    public void testParseWithCustomParser() {
        String html = "<xml><message>Hello World</message></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document document = Jsoup.parse(html, "https://example.com", parser);
        Assert.assertEquals("Hello World", document.select("message").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><base href='/'><body><a href='about.html'>About</a></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals("/about.html", document.baseUri());
        Assert.assertEquals("About", document.select("a[href='about.html']").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectWithMalformedUrl() {
        Jsoup.connect("javascript:alert('Hello')");
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharset() {
        File file = new File("invalid_charset.html");
        Jsoup.parse(file, "invalid_charset", "https://example.com");
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/invalid_charset.html");
        Jsoup.parse(inputStream, "invalid_charset", "https://example.com");
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello World</h1></body>";
        Document document = Jsoup.parseBodyFragment(html);
        Assert.assertEquals("<h1>Hello World</h1>", document.html());
    }

    @Test
    public void testCleanWithWhitelist() {
        String html = "<html><head><title>Test</title><script src='script.js'></script></head><body><script src='script2.js'></script><iframe src='malicious.com'></iframe></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(html, whitelist);
        Document document = Jsoup.parse(cleanedHtml);
        Assert.assertFalse(document.body().select("script, iframe").isEmpty());
        Assert.assertTrue(document.body().select("title").text().contains("Test"));
    }

    @Test
    public void testCleanWithCustomBaseUri() {
        String html = "<html><head><title>Test</title><base href='/test/'><script src='script.js'></script></head><body><script src='test/script2.js'></script><iframe src='malicious.com'></iframe></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(html, whitelist, "/test/");
        Document document = Jsoup.parse(cleanedHtml);
        Assert.assertFalse(document.body().select("script, iframe").isEmpty());
        Assert.assertTrue(document.body().select("title").text().contains("Test"));
    }

    @Test
    public void testIsValid() {
        String html = "<html><head><title>Test</title></head><body><form method='post' action='submit.php'><input type='text' name='name' value='John'></form></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("html", "head", "title", "form");
        Assert.assertTrue(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testIsNotValid() {
        String html = "<html><head><title>Test</title><script>alert('Hello')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("html", "head", "title");
        Assert.assertFalse(Jsoup.isValid(html, whitelist));
    }
}