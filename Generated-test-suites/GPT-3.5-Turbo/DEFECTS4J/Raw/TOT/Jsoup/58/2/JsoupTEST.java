import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.*;
import static org.junit.Assert.*;

public class JsoupTest {

    // Test case for parse(String html, String baseUri)
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        String title = doc.title();
        assertEquals("Hello World!", title);
    }

    // Test case for parse(String html, String baseUri, Parser parser)
    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri, Parser.htmlParser());
        String title = doc.title();
        assertEquals("Hello World!", title);
    }

    // Test case for parse(String html)
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        String title = doc.title();
        assertEquals("Hello World!", title);
    }

    // Test case for connect(String url)
    @Test
    public void testConnect() {
        String url = "https://example.com";
        Connection con = Jsoup.connect(url);
        assertNotNull(con);
    }

    // Test case for parse(File in, String charsetName, String baseUri)
    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("path/to/file.html");
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(file, charset, baseUri);
        String title = doc.title();
        assertEquals("Title", title);
    }

    // Test case for parse(File in, String charsetName)
    @Test
    public void testParseFromFileWithoutBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        String charset = "UTF-8";
        Document doc = Jsoup.parse(file, charset);
        String title = doc.title();
        assertEquals("Title", title);
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri)
    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = new FileInputStream("path/to/input.html");
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(in, charset, baseUri);
        String title = doc.title();
        assertEquals("Title", title);
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("path/to/input.html");
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(in, charset, baseUri, Parser.htmlParser());
        String title = doc.title();
        assertEquals("Title", title);
    }

    // Test case for parseBodyFragment(String bodyHtml, String baseUri)
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String title = doc.title();
        assertEquals("Hello World!", title);
    }

    // Test case for parseBodyFragment(String bodyHtml)
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        String title = doc.title();
        assertEquals("Hello World!", title);
    }

    // Test case for parse(URL url, int timeoutMillis)
    @Test
    public void testParseFromURL() throws IOException {
        URL url = new URL("https://example.com");
        int timeoutMillis = 5000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        String title = doc.title();
        assertEquals("Title", title);
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist)
    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello World!</h1><script>alert('XSS');</script>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertFalse(cleanHtml.contains("<script>"));
    }

    // Test case for clean(String bodyHtml, Whitelist whitelist)
    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1><script>alert('XSS');</script>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertFalse(cleanHtml.contains("<script>"));
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist, Document.OutputSettings outputSettings)
    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1><script>alert('XSS');</script>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml).prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertFalse(cleanHtml.contains("<script>"));
        assertTrue(cleanHtml.endsWith("</h1>"));
    }

    // Test case for isValid(String bodyHtml, Whitelist whitelist)
    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}