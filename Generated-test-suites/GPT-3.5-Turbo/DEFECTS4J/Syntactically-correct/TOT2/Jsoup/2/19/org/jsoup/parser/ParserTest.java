import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";

        Document parsedDoc = Parser.parse(html, baseUri);

        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals("Test", parsedDoc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://example.com";

        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        assertEquals("Hello", parsedDoc.body().select("h1").text());
    }

    // Test edge case: empty input for parse method
    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyInput() {
        Parser.parse("", "http://example.com");
    }

    // Test edge case: empty input for parseBodyFragment method
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentEmptyInput() {
        Parser.parseBodyFragment("", "http://example.com");
    }

    // Test error scenario: invalid base URI for parse method
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBaseUri() {
        Parser.parse("<html></html>", "");
    }
}