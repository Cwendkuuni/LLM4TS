import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.safety.*;

import java.io.*;
import java.net.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test Parse</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test Parse", doc.select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Test Parse with Parser</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test Parse with Parser", doc.select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Test Parse without BaseUri</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test Parse without BaseUri", doc.select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection conn = Jsoup.connect(url);
        assertNotNull(conn);
        assertEquals(url, conn.request().url());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test Parse File", doc.select("h1").text());
    }

    @Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        assertNotNull(doc);
        assertEquals("Test Parse File without BaseUri", doc.select("h1").text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test Parse InputStream", doc.select("h1").text());
        in.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test Parse InputStream with Parser", doc.select("h1").text());
        in.close();
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Parse Body Fragment</h1>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test Parse Body Fragment", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Test Parse Body Fragment without BaseUri</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Test Parse Body Fragment without BaseUri", doc.select("h1").text());
    }

    @Test
    public void testParseURL() throws IOException {
        String url = "http://example.com";
        int timeoutMillis = 10000;
        Document doc = Jsoup.parse(new URL(url), timeoutMillis);
        assertNotNull(doc);
        assertEquals("Test Parse URL", doc.select("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Test Clean</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<h1>Test Clean</h1>", result);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Test Clean without BaseUri</h1>";
        Whitelist whitelist = Whitelist.basic();
        String result = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<h1>Test Clean without BaseUri</h1>", result);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Test Clean with OutputSettings</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<h1>Test Clean with OutputSettings</h1>", result);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Test IsValid</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean invalid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(invalid);
    }
}