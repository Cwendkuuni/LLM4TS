import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
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

public class JsoupTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello Jsoup</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection con = Jsoup.connect(url);
        assertNotNull(con);
        assertEquals(url, con.request().url().toString());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = DataUtil.loadStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<p>Hello <b>Jsoup</b></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<p>Hello <b>Jsoup</b></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Hello <b>Jsoup</b></p>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}