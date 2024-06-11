import org.jsoup.Jsoup;
import org.jsoup.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithBaseUriAndParser() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        String html = null;
        Document doc = Jsoup.parse(html);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyHtml() {
        String html = "";
        Document doc = Jsoup.parse(html);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charsetName);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullCharsetName() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, null, "");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        assertNotNull(doc);
    }

    @Test
    public void testParseWithInvalidUrl() {
        String url = "file://test.html";
        assertThrows(IllegalArgumentException.class, () -> Jsoup.parse(url));
    }

    @Test
    public void testParseWithMalformedUrl() {
        String url = "http://example.com:8080/test.html@user:password";
        assertThrows(MalformedURLException.class, () -> Jsoup.parse(url));
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<html><body><script>alert('Hello World')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assert cleanHtml.isEmpty();
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src=\"image.jpg\" />";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, baseUri);
        assertNotNull(cleanHtml);
        assert cleanHtml.isEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void testCleanWithNullBodyHtml() {
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(null, whitelist);
    }

    @Test
    public void testCleanWithEmptyBodyHtml() {
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean("", whitelist);
        assertNotNull(cleanHtml);
        assert cleanHtml.isEmpty();
    }

    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><p>Hello World<script>alert('Hello World')</script></p></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, baseUri, outputSettings);
        assertNotNull(cleanHtml);
        assert !cleanHtml.isEmpty();
        assert cleanHtml.contains("<p>Hello World</p>");
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<html><body><p id=\"test\" class=\"test\">Hello World<script>alert('Hello World')</script></p></body></html>";
        Whitelist whitelist = Whitelist.simpleText().addTag("p");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assert isValid;
    }

    @Test
    public void testIsValidWithInvalidTag() {
        String bodyHtml = "<html><body><p id=\"test\" class=\"test\">Hello World<script>alert('Hello World')</script></p><div>Hello World</div></body></html>";
        Whitelist whitelist = Whitelist.simpleText().addTag("p");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assert !isValid;
    }

}