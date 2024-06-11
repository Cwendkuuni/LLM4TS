import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.IOException;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseString() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = Jsoup.parse(html);
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testConnectAndGet() throws IOException {
        Connection connection = Jsoup.connect("http://example.com");
        Document document = connection.get();
        assertEquals("Example Domain", document.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "invalid";
        Jsoup.parse(file, charsetName, "");
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(inputStream, "", parser);
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml, "");
        assertEquals("Hello World", document.body().text());
    }

    @Test(expected = IOException.class)
    public void testParseStringWithInvalidHtml() throws IOException {
        String html = "<html><body<p>Hello World</p>";
        Jsoup.parse(html);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<html><head><script src='script.js'></script><p id='test'>Hello World</p></head><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<p id='test'>Hello World</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><head><title>Test</title><script>alert('XSS')</script></head><body><p>Hello World</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testParseUrl() throws IOException {
        Document document = Jsoup.parse("http://example.com");
        assertEquals("Example Domain", document.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        String baseUri = null;
        Jsoup.parse(file, "", baseUri);
    }

}