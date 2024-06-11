import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseNormalHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://test.com");
        assertTrue(doc.body().text().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        try {
            Parser.parse(html, "http://test.com");
        } catch (IllegalArgumentException e) {
            fail("Expected IllegalArgumentException, but got " + e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertEquals("Hello World", doc.body().text());
    }

    // Add test cases for edge cases and exception handling scenarios here
}