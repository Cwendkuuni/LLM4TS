import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        // Add more asserts to check the parsed content
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        // Add more asserts to check the parsed content
    }

    @Test
    public void testParse_comment() {
        // Edge case: parsing a comment with special characters
        String html = "<!--Test comment with special characters: &amp; &gt; -->";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        // Assert the presence of the comment in the parsed document
    }

    @Test
    public void testParse_emptyTag() {
        // Edge case: parsing an empty tag
        String html = "<img src='image.jpg'/>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        // Verify that the empty tag is properly handled
    }

    @Test
    public void testParse_exception() {
        // Error scenario: parsing invalid HTML
        String html = "<div><p>Missing closing tag";
        try {
            Document doc = Parser.parse(html, "http://example.com");
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // Expected exception
        }
    }
}