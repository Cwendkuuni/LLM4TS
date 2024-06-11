import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import static org.junit.Assert.*;

public class JsoupTest {

    private String html;
    private String baseUri;
    private Parser parser;
    private File file;
    private String charsetName;
    private String bodyHtml;
    private Whitelist whitelist;
    private Document.OutputSettings outputSettings;

    @Before
    public void setUp() {
        html = "<html><body><h1>Test HTML</h1></body></html>";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
        file = new File("test.html");
        charsetName = "UTF-8";
        bodyHtml = "<p>Hello, <b>World!</b></p>";
        whitelist = Whitelist.basic();
        outputSettings = new Document.OutputSettings().prettyPrint(false);
    }

    @Test
    public void testParseStringString_shouldReturnDocumentObject() {
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseStringStringParser_shouldReturnDocumentObject() {
        Document document = Jsoup.parse(html, baseUri, parser);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseString_shouldReturnDocumentObject() {
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testConnect_shouldReturnConnectionObject() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testParseFileStringString_shouldReturnDocumentObject() throws IOException {
        Document document = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseFileString_shouldReturnDocumentObject() throws IOException {
        Document document = Jsoup.parse(file, charsetName);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseInputStreamStringString_shouldReturnDocumentObject() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamStringStringParser_shouldReturnDocumentObject() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(html, document.html());
        inputStream.close();
    }

    @Test
    public void testParseBodyFragmentStringString_shouldReturnDocumentObject() {
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.html());
    }

    @Test
    public void testParseBodyFragmentString_shouldReturnDocumentObject() {
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals(bodyHtml, document.html());
    }

    @Test
    public void testParseURLInt_shouldReturnDocumentObject() throws IOException {
        URL url = new URL("http://example.com");
        Document document = Jsoup.parse(url, 5000);
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testCleanStringStringWhitelist_shouldReturnCleanHTML() {
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("Hello, World!", cleanedHtml);
    }

    @Test
    public void testCleanStringWhitelist_shouldReturnCleanHTML() {
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("Hello, World!", cleanedHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings_shouldReturnCleanHTML() {
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("Hello, World!", cleanedHtml);
    }

    @Test
    public void testIsValid_shouldReturnTrue() {
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }

}