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

    @Test
    public void testParseStringString() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com");
        assertTrue(doc.title().equals("Test"));
    }

    @Test
    public void testParseStringStringParser() {
        Document doc = Jsoup.parse("<html><body><p>Hello World</p></body></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseString() {
        Document doc = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        assertNotNull(doc);
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
    }

    @Test
    public void testParseException() {
        File file = new File("invalid.html");
        assertThrows(IOException.class, () -> Jsoup.parse(file, "UTF-8", "http://example.com"));
    }

    @Test
    public void testParseFileString() {
        File file = new File("valid.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamString() {
        InputStream is = getClass().getResourceAsStream("sample.html");
        Document doc = Jsoup.parse(is, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamStringParser() {
        InputStream is = getClass().getResourceAsStream("sample.xml");
        Document doc = Jsoup.parse(is, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentString() {
        String html = "<div><p>Hello World</p></div>";
        Document doc = Jsoup.parseBodyFragment(html, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<div><p>Hello World</p></div>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertNotNull(doc);
    }

    @Test
    public void testParseURLInt() {
        URL url = new URL("http://example.com");
        assertThrows(IOException.class, () -> Jsoup.parse(url, 1000));
    }

    @Test
    public void testCleanStringStringWhitelist() {
        String dirtyHtml = "<script>alert('XSS')</script><p>Hello World</p>";
        String cleanHtml = Jsoup.clean(dirtyHtml, "http://example.com", Whitelist.basic());
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testCleanStringWhitelist() {
        String dirtyHtml = "<script>alert('XSS')</script><p>Hello World</p>";
        String cleanHtml = Jsoup.clean(dirtyHtml, Whitelist.basic());
        assertFalse(cleanHtml.contains("script"));
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        String dirtyHtml = "<div><p>Hello World</p></div>";
        String cleanHtml = Jsoup.clean(dirtyHtml, "http://example.com", Whitelist.basic(), Document.OutputSettings.html());
        assertEquals("<p>Hello World</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<p>Hello World</p>";
        boolean valid = Jsoup.isValid(html, Whitelist.basic());
        assertTrue(valid);
    }
}