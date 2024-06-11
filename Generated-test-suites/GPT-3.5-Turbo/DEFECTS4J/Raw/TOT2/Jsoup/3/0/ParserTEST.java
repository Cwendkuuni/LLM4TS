import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertNotNull(doc);
        assertEquals("Test Page", doc.head().select("title").text());
        assertEquals("Hello, World!", doc.body().select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><span>Relaxed Parsing</span></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(doc);
        assertEquals("Relaxed Parsing", doc.body().select("span").text());
    }

    // Additional test cases for edge cases and exceptions can be added here

}