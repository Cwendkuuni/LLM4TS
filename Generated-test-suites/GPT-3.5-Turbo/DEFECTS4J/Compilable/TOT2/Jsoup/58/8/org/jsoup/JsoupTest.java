package org.jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    // Test case for parse(String html, String baseUri)
    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    // Test case for parse(String html)
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello Jsoup</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello Jsoup", doc.select("h1").text());
    }

    // Test case for connect(String url)
    @Test
    public void testConnectWithValidUrl() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
    }

    // Test case for parse(File in, String charsetName, String baseUri) with IOException
    @Test(expected = IOException.class)
    public void testParseFromFileWithIOException() throws IOException {
        File file = new File("nonexistent.html");
        Jsoup.parse(file, "UTF-8", "http://example.com");
    }

    // Test case for parse(InputStream in, String charsetName, String baseUri) with IOException
    @Test(expected = IOException.class)
    public void testParseFromInputStreamWithIOException() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Jsoup.parse(inputStream, "UTF-8", "http://example.com");
    }

    // Test case for clean(String bodyHtml, String baseUri, Whitelist whitelist)
    @Test
    public void testCleanBodyHtmlWithWhitelist() {
        String bodyHtml = "<div><script>alert('XSS')</script></div>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, "http://example.com", whitelist);
        assertFalse(cleanHtml.contains("script"));
    }

    // Test case for isValid(String bodyHtml, Whitelist whitelist)
    @Test
    public void testIsValidBodyHtmlWithWhitelist() {
        String bodyHtml = "<p>This is a <b>bold</b> text.</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(valid);
    }

}