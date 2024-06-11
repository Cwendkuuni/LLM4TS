import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParse() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseWithParser() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document doc = Jsoup.parse("<html></html>");
        assertNotNull(doc);
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        Document doc = Jsoup.parse(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseFileWithoutCharsetName() throws IOException {
        Document doc = Jsoup.parse(new File("test.html"), "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<div></div>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String cleanedHtml = Jsoup.clean("<script></script>", "http://example.com", Whitelist.basic());
        assertTrue(cleanedHtml.contains("<div>"));
    }

    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid("<div></div>", Whitelist.basic());
        assertTrue(valid);
    }
}