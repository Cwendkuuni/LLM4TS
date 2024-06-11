import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

    @Test
    public void testParse() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "https://www.example.com");
        assertEquals("Document should be created successfully", doc.toString(), "<html>\n <head></head>\n <body></body>\n</html>");
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("https://www.example.com");
        assertEquals("Connection should be established successfully", con.url().toString(), "https://www.example.com");
    }

    @Test
    public void testParseFile() {
        File file = new File("test.html");
        try {
            Document doc = Jsoup.parse(file, "UTF-8", "https://www.example.com");
            assertEquals("Document should be created successfully from file", doc.title(), "Test Document");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseStream() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document doc = Jsoup.parse(inputStream, "UTF-8", "https://www.example.com");
            assertTrue(doc.hasText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<div>Hello</div>", "https://www.example.com");
        assertEquals("Body fragment should be parsed successfully", doc.body().text(), "Hello");
    }

    @Test
    public void testClean() {
        String cleanedHtml = Jsoup.clean("<script>alert('xss')</script>", Whitelist.basic());
        assertEquals("HTML should be cleaned successfully", cleanedHtml, "");
    }

    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid("<p>Valid</p>", Whitelist.basic());
        assertTrue("HTML should be considered valid", valid);
    }
}