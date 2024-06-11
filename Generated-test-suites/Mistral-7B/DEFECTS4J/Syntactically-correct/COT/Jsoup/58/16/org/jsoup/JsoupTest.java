import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='https://example.com'></base></head><body><a href='/about'>About</a></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><div>Test</div></body></html>";
        String baseUri = "";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        Assert.assertTrue(doc.body().child(0) instanceof Element);
        Assert.assertEquals("Test", doc.body().child(0).text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><base href='/'></base></head><body><a href='/about'>About</a></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testConnect() {
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
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "invalidCharset");
    }

    @Test
    public void testClean() {
        String html = "<html><head><script src='malicious.js'></script></head><body><h1>Test</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, whitelist);
        Document doc = Jsoup.parse(cleanHtml);
        Assert.assertFalse(doc.select("script").size() > 0);
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><input type='text' name='name' value='John Doe'></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Assert.assertTrue(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testIsValidWithInvalidInput() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Assert.assertFalse(Jsoup.isValid(html, whitelist));
    }
}