import org.junit.*;
import static org.junit.Assert.*;
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

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
    }

    @Test
    public void testConnect() throws IOException {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
        assertEquals("http://example.com", con.url().toString());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
        file.delete();
    }

    @Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
        file.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(in, charsetName, baseUri);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
        in.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(in, charsetName, baseUri, parser);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
        in.close();
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 1000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        assertEquals("Title", doc.title());
        assertEquals("h1", doc.getElementsByTag("h1").first().tagName());
        assertEquals("p", doc.getElementsByTag("p").first().tagName());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<h1>Title</h1><p>Paragraph</p>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<h1>Title</h1><p>Paragraph</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<h1>\n Title\n</h1>\n<p>\n Paragraph\n</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        Whitelist whitelist = Whitelist.basic();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(valid);
    }
}