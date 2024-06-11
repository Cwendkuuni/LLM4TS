import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class JsoupTest {

    @Test
    public void testParse_StringAndBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        Element title = document.title();
        assertEquals("Test", title.text());
    }

    @Test
    public void testParse_StringAndBaseUri_Parser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());
        assertNotNull(document);
        Element title = document.title();
        assertEquals("Test", title.text());
    }

    @Test
    public void testParse_String() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        Element title = document.title();
        assertEquals("Test", title.text());
    }

    @Test
    public void testConnect_Url() {
        Connection connection = Jsoup.connect("http://example.com");
        Document document = connection.get();
        assertNotNull(document);
    }

    @Test
    public void testParse_FileAndCharsetNameAndBaseUri() throws IOException {
        File inputFile = new File("input.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParse_FileAndCharsetName() throws IOException {
        File inputFile = new File("input.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputFile, charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParse_InputStreamAndCharsetNameAndBaseUri() throws IOException {
        File inputFile = new File("input.html");
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParse_InputStreamAndCharsetName() throws IOException {
        File inputFile = new File("input.html");
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(inputStream, charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment_StringAndBaseUri() {
        String bodyHtml = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment_String() {
        String bodyHtml = "<html><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParse_Url_IOException() {
        Document document = Jsoup.parse("http://example.com/invalid-url");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_Null() {
        assertThrows(IllegalArgumentException.class, () -> Jsoup.parse(null));
    }

    @Test
    public void testClean_StringAndBaseUriAndWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testClean_StringAndWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testClean_StringAndBaseUriAndWhitelist_OutputSettings() {
        String bodyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testIsValid_StringAndWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}