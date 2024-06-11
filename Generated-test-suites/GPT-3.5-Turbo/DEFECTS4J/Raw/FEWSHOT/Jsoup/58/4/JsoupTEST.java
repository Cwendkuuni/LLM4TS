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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        Document document = Jsoup.parse(html);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        String baseUri = "https://www.example.com/";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertEquals(html, document.html());
    }

    @Test
    public void testConnect() {
        String url = "https://www.example.com/";
        Connection connection = Jsoup.connect(url);
        assertEquals(url, connection.url());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "");
        assertTrue(document.hasText());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        assertTrue(document.hasText());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "https://www.example.com/";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(bodyHtml, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello World!</p>";
        String baseUri = "https://www.example.com/";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<p>Hello World!</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello World!</p>";
        String baseUri = "https://www.example.com/";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<p>\n" +
                " Hello World!\n" +
                "</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

}