import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);

        // Typical use case
        assertEquals("Test", doc.title());

        // Edge case: empty HTML
        html = "";
        doc = parser.parse(html, baseUri);
        assertEquals("", doc.title());

        // Error scenario: null HTML
        html = null;
        doc = parser.parse(html, baseUri);
        assertEquals(null, doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        // Typical use case
        assertEquals("Hello", doc.body().getElementsByTag("h1").text());

        // Edge case: empty body HTML
        bodyHtml = "";
        doc = parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("", doc.body().html());

        // Error scenario: null body HTML
        bodyHtml = null;
        doc = parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(null, doc);
    }

    @Test
    public void testParseRelaxed() {
        // This method is private and cannot be tested directly.
    }

}