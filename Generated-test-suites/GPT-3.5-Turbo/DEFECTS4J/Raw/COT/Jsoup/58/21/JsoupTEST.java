import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(html, baseUri);

        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document doc = Jsoup.parse(html, baseUri, parser);

        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";

        Document doc = Jsoup.parse(html);

        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";

        Connection connection = Jsoup.connect(url);

        assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(file, charsetName, baseUri);

        assertNotNull(doc);
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";

        Document doc = Jsoup.parse(file, charsetName);

        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(inputStream, charsetName, baseUri);

        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";

        Document doc = Jsoup.parse(inputStream, charsetName);

        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";

        Document doc = Jsoup.parseBodyFragment(bodyHtml);

        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseURLWithTimeout() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;

        Document doc = Jsoup.parse(url, timeoutMillis);

        assertNotNull(doc);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<div><script>alert('Hello World!');</script></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        assertFalse(cleanHtml.contains("<script>"));
    }

    @Test
    public void testCleanWithWhitelistWithoutBaseUri() {
        String bodyHtml = "<div><script>alert('Hello World!');</script></div>";
        Whitelist whitelist = Whitelist.basic();

        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        assertFalse(cleanHtml.contains("<script>"));
    }

    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<div><script>alert('Hello World!');</script></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        assertFalse(cleanHtml.contains("<script>"));
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<div><script>alert('Hello World!');</script></div>";
        Whitelist whitelist = Whitelist.basic();

        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        assertFalse(isValid);
    }
}