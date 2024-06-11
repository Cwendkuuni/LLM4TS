import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection;

import org.junit.Test;
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
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
    }

    @Test
    public void testParseFile() {
        Document doc = Jsoup.parse(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() {
        InputStream in = new ByteArrayInputStream("<html></html>".getBytes());
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<div></div>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String cleanedHtml = Jsoup.clean("<div>untrusted</div>", "http://example.com", Whitelist.basic());
        assertNotNull(cleanedHtml);
    }

    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid("<div></div>", Whitelist.basic());
        assertTrue(valid);
    }
}