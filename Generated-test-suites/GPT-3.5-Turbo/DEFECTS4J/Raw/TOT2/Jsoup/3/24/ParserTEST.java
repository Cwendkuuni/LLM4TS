import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Test typical use case
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().selectFirst("h1").text());

        // Test edge cases
        // Empty HTML input
        String emptyHtml = "";
        Document emptyDoc = Parser.parse(emptyHtml, baseUri);
        assertNotNull(emptyDoc);
        assertTrue(emptyDoc.getAllElements().isEmpty());

        // Error scenario: null parameters
        try {
            Parser.parse(null, baseUri);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragment() {
        // Test typical use case
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().selectFirst("h1").text());

        // Test edge cases
        // Fragment with empty body
        String emptyHtml = "";
        Document emptyDoc = Parser.parseBodyFragment(emptyHtml, baseUri);
        assertNotNull(emptyDoc);
        assertTrue(emptyDoc.body().children().isEmpty());

        // Error scenario: null parameters
        try {
            Parser.parse(null, baseUri);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Test typical use case
        String bodyHtml = "<div><span>Test</span></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.body().selectFirst("span").text());

        // Test edge cases
        // Fragment with missing tags
        String missingTagsHtml = "<title>Hello</title>";
        Document missingTagsDoc = Parser.parseBodyFragmentRelaxed(missingTagsHtml, baseUri);
        assertNotNull(missingTagsDoc);
        assertTrue(missingTagsDoc.select("html").isEmpty());

        // Error scenario: null parameters
        try {
            Parser.parseBodyFragmentRelaxed(null, baseUri);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

}