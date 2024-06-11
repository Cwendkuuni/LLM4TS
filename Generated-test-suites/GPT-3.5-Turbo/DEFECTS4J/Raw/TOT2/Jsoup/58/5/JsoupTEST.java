import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

    @Test
    public void testParseStringWithStringBaseUri() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseStringWithStringBaseUriAndParser() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseURLWithInvalidURL() throws IOException {
        Jsoup.parse(new URL("invalidUrl"), 1000);
    }

    @Test(expected = HttpStatusException.class)
    public void testParseURLWithInvalidResponse() throws IOException {
        Jsoup.parse(new URL("http://example.com/error"), 1000);
    }

    @Test
    public void testParseFileWithStringCharsetStringBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test(expected = SocketTimeoutException.class)
    public void testParseURLWithTimeout() throws IOException {
        Jsoup.parse(new URL("http://example.com"), 1);
    }

    @Test
    public void testParseInputStreamWithStringCharsetStringBaseUri() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCleanStringWithWhitelist() {
        String cleanedHtml = Jsoup.clean("<div><script>alert('XSS');</script></div>", Whitelist.basic());
        assertEquals("<div></div>", cleanedHtml);
    }

    @Test
    public void testIsValidWithValidHtml() {
        boolean isValid = Jsoup.isValid("<p>Hello, World!</p>", Whitelist.simpleText());
        assertTrue(isValid);
    }

}