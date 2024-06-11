import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("", doc.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        String html = null;
        String baseUri = "https://www.example.com";
        Parser.parse(html, baseUri);
    }

    // Add more test cases for edge cases and exception handling scenarios

}