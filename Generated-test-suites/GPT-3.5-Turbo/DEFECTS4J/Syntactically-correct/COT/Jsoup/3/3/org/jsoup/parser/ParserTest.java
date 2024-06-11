import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Test if Document is parsed correctly
        assertEquals("Test Page", doc.title());
        assertEquals("Welcome", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String fragment = "<h1>Welcome</h1><p>This is a fragment</p>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(fragment, baseUri);

        // Test if fragment is parsed correctly
        assertEquals("Welcome", doc.body().select("h1").text());
        assertEquals("This is a fragment", doc.body().select("p").text());
    }

    // Test edge case: Empty HTML input
    @Test
    public void testParse_EmptyInput() {
        String html = "";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNull(doc);
    }

    // Test edge case: Empty body fragment input
    @Test
    public void testParseBodyFragment_EmptyInput() {
        String fragment = "";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(fragment, baseUri);

        assertNull(doc.body().html());
    }

    // Test exception handling for invalid HTML input
    @Test(expected = RuntimeException.class)
    public void testParse_InvalidInput() {
        String html = "<h1>Welcome</h2>"; // Invalid closing tag
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
    }
}