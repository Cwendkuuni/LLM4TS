import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class ParserTest {
    // 

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseNullHtml() {
        String baseUri = "http://example.com";
        try {
            Parser.parse(null, baseUri);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        try {
            Parser.parse(html, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", doc.html());
    }

    @Test
    public void testParseBodyFragmentNullHtml() {
        String baseUri = "http://example.com";
        try {
            Parser.parseBodyFragment(null, baseUri);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBodyFragmentNullBaseUri() {
        String bodyHtml = "<div><p>Test</p></div>";
        try {
            Parser.parseBodyFragment(bodyHtml, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // 
}