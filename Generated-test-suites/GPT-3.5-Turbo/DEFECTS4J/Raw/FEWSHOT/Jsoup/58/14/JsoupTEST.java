package org.jsoup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    @Test
    public void testParseStringString() {
        Document document = Jsoup.parse("<html><head></head><body>Hello World</body></html>", "https://www.example.com");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseStringStringParser() {
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse("<html><head></head><body>Hello World</body></html>", "https://www.example.com", parser);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseString() {
        Document document = Jsoup.parse("<html><head></head><body>Hello World</body></html>");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://www.example.com");
        assertEquals("https://www.example.com", connection.request().url().toString());
    }

    @Test
    public void testParseFileStringString() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "https://www.example.com");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseFileString() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseInputStreamStringString() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "https://www.example.com");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseInputStreamStringStringParser() throws IOException {
        Parser parser = Parser.xmlParser();
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "https://www.example.com", parser);
        assertEquals("Hello World", document.body().text()); 
    }

    @Test
    public void testParseBodyFragmentStringString() {
        Document document = Jsoup.parseBodyFragment("<div><p>Hello World</p></div>", "https://www.example.com");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseBodyFragmentString() {
        Document document = Jsoup.parseBodyFragment("<div><p>Hello World</p></div>");
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseURLInt() throws IOException {
        URL url = new URL("https://www.example.com");
        Document document = Jsoup.parse(url, 5000);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testCleanStringStringWhitelist() {
        String cleanHtml = Jsoup.clean("<div><script>alert('Hello World');</script></div>", "https://www.example.com", Whitelist.basic());
        assertEquals("<div></div>", cleanHtml);
    }

    @Test
    public void testCleanStringWhitelist() {
        String cleanHtml = Jsoup.clean("<div><script>alert('Hello World');</script></div>", Whitelist.basic());
        assertEquals("<div></div>", cleanHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean("<div><script>alert('Hello World');</script></div>", "https://www.example.com", Whitelist.basic(), outputSettings);
        assertEquals("<div></div>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        assertTrue(Jsoup.isValid("<div><p>Hello World</p></div>", Whitelist.basic()));
        assertFalse(Jsoup.isValid("<div><script>alert('Hello World');</script></div>", Whitelist.basic()));
    }
}