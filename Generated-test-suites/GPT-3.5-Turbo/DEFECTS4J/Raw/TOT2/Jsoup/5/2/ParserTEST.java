import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    // Add more test cases for edge cases and error scenarios

}