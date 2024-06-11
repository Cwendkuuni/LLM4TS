import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import java.io.IOException;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);

        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testConnectAndGet() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();

        Assert.assertNotNull(document);
        Assert.assertEquals("Example Domain", document.title());
    }

    @Test
    public void testParseFileWithBaseUriAndCharsetName() throws IOException {
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(filePath, charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("File Title", document.title());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(filePath, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("File Title", document.title());
    }

    @Test
    public void testParseInputStreamWithBaseUriAndCharsetName() throws IOException {
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(new FileInputStream(filePath), charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("File Title", document.title());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new FileInputStream(filePath), baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("File Title", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);

        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('XSS')</script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);

        Assert.assertNotNull(cleanedHtml);
        Assert.assertEquals("Hello World", cleanedHtml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new Jsoup();
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriAndHtml() {
        Document document = Jsoup.parse(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = null;
        Document document = Jsoup.parse(html, baseUri, parser);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCharsetName() throws IOException {
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(filePath, null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(null, charsetName, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = null;
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriAndInputStream() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document document = Jsoup.parse(inputStream, charsetName, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBodyHtml() {
        Whitelist whitelist = Whitelist.simpleText();
        String cleanedHtml = Jsoup.clean(null, whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testNullWhitelist() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        String cleanedHtml = Jsoup.clean(bodyHtml, null);
    }

    @Test
    public void testIsValidWithAllowedTags() {
        String bodyHtml = "<html><body><h1 id=\"title\">Hello World</h1><p>This is a paragraph.</p></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithDisallowedTags() {
        String bodyHtml = "<html><body><h1 id=\"title\">Hello World</h1><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }

}