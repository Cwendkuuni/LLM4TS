import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(html, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello Jsoup</h1></body></html>";
        
        Document doc = Jsoup.parse(html);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        
        Connection conn = Jsoup.connect(url);
        
        assertNotNull(conn);
        assertEquals(url, conn.url());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseFromFileWithoutCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        
        Document doc = Jsoup.parse(file, charsetName);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.body().select("h1").text());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        
        Document doc = Jsoup.parse(url, timeoutMillis);
        
        assertNotNull(doc);
        assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1><script>alert('Hello');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        assertEquals("<h1></h1>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1><script>alert('Hello');</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        assertEquals("<h1></h1>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1><script>alert('Hello');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        assertEquals("<h1>\n" +
                     "</h1>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        assertTrue(isValid);
    }
    
}