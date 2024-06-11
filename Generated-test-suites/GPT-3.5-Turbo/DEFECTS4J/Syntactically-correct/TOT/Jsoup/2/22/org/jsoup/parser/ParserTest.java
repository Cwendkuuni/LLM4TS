import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertEquals("<html>", doc.child(0).toString());
        assertEquals("<head>", doc.head().toString());
        assertEquals("<title>Test Page</title>", doc.title().toString());
        assertEquals("<body>", doc.body().toString());
        assertEquals("<h1>Welcome</h1>", doc.body().child(0).toString());

        // Edge case: Empty HTML
        html = "";
        doc = Parser.parse(html, baseUri);
        assertEquals("", doc.toString());

        // Error scenario: Null HTML
        try {
            html = null;
            doc = Parser.parse(html, baseUri);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("html must not be null", e.getMessage());
        }

        // Error scenario: Null baseUri
        try {
            html = "<html></html>";
            baseUri = null;
            doc = Parser.parse(html, baseUri);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<h1>Welcome</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("<body>", doc.child(0).toString());
        assertEquals("<h1>Welcome</h1>", doc.body().child(0).toString());

        // Edge case: Empty fragment
        bodyHtml = "";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("", doc.toString());

        // Error scenario: Null bodyHtml
        try {
            bodyHtml = null;
            doc = Parser.parseBodyFragment(bodyHtml, baseUri);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("bodyHtml must not be null", e.getMessage());
        }

        // Error scenario: Null baseUri
        try {
            bodyHtml = "<h1>Welcome</h1>";
            baseUri = null;
            doc = Parser.parseBodyFragment(bodyHtml, baseUri);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }
}