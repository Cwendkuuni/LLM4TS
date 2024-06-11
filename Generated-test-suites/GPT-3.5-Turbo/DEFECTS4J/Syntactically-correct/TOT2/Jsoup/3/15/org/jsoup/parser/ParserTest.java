import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        // Test with relaxed parsing enabled
        // Edge case: Verify that implicit tags are not automatically created when missing
        String html = "<div><span>Hello World!</span>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("span").text());
    }

    @Test
    public void testParseWithInvalidHTML() {
        // Test with invalid HTML input
        // Error scenario: Verify proper handling of invalid HTML
        String html = "<html><body><h1>Invalid HTML";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertTrue(doc.title().isEmpty());
        assertTrue(doc.body().text().contains("Invalid HTML"));
    }

    @Test
    public void testParseComment() {
        // Test parsing an HTML comment
        String commentHtml = "<!-- This is a comment -->";
        Parser parser = new Parser(commentHtml, "http://example.com", true);
        parser.parseComment();
        Document doc = parser.doc;
        assertEquals("<!-- This is a comment -->", doc.body().html());
    }

    // Add more test cases for other methods in the Parser class as needed

}