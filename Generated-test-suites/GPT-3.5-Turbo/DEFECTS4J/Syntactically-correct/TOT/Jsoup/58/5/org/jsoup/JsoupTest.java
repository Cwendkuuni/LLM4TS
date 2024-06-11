import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void parse_withHtmlAndBaseUri_returnsDocument() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document document = Jsoup.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parse_withHtmlBaseUriAndParser_returnsDocument() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parse_withHtml_returnsDocument() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        
        Document document = Jsoup.parse(html);
        
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals("", document.baseUri());
    }

    @Test
    public void connect_withUrl_returnsConnection() {
        String url = "https://example.com";
        
        Connection connection = Jsoup.connect(url);
        
        assertNotNull(connection);
        assertEquals(url, connection.url());
    }

    @Test
    public void parse_withFileCharsetNameAndBaseUri_returnsDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("", document.html());
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    @Test
    public void parse_withFileAndCharsetName_returnsDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(file, charsetName);
        
        assertNotNull(document);
        assertEquals("", document.html());
        assertEquals(file.getAbsolutePath(), document.baseUri());
    }

    @Test
    public void parse_withInputStreamCharsetNameAndBaseUri_returnsDocument() throws IOException {
        InputStream inputStream = DataUtil.loadStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parse_withInputStreamCharsetNameBaseUriAndParser_returnsDocument() throws IOException {
        InputStream inputStream = DataUtil.loadStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parseBodyFragment_withBodyHtmlAndBaseUri_returnsDocument() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parseBodyFragment_withBodyHtml_returnsDocument() {
        String bodyHtml = "<h1>Hello World!</h1>";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals("", document.baseUri());
    }

    @Test
    public void parse_withUrlAndTimeoutMillis_returnsDocument() throws IOException {
        URL url = new URL("https://example.com");
        int timeoutMillis = 10000;
        
        Document document = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(document);
        assertTrue(document.html().length() > 0);
        assertEquals("", document.baseUri());
    }

    @Test
    public void clean_withBodyHtmlBaseUriAndWhitelist_returnsCleanedHtml() {
        String bodyHtml = "<h1>Hello <script>World!</script></h1>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertFalse(cleanedHtml.contains("<script>"));
        assertTrue(cleanedHtml.contains("<h1>Hello World!</h1>"));
    }

    @Test
    public void clean_withBodyHtmlAndWhitelist_returnsCleanedHtml() {
        String bodyHtml = "<h1>Hello <script>World!</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertFalse(cleanedHtml.contains("<script>"));
        assertTrue(cleanedHtml.contains("<h1>Hello World!</h1>"));
    }

    @Test
    public void clean_withBodyHtmlBaseUriWhitelistAndOutputSettings_returnsCleanedHtml() {
        String bodyHtml = "<h1>Hello <script>World!</script></h1>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertFalse(cleanedHtml.contains("<script>"));
        assertTrue(cleanedHtml.contains("<h1>Hello World!</h1>"));
    }
    
    @Test
    public void isValid_withBodyHtmlAndWhitelist_returnsTrue() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }

    @Test
    public void isValid_withBodyHtmlAndInvalidWhitelist_returnsFalse() {
        String bodyHtml = "<h1>Hello <script>World!</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertFalse(isValid);
    }
}