import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Hello, World!", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello, World!</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("Hello, World!", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<div></div><span></span>", "http://example.com");
        assertNotNull(doc);
        assertEquals(2, doc.children().size());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithNullHtml() {
        try {
            Parser.parse(null, "http://example.com");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testParseBodyFragmentWithNullHtml() {
        try {
            Parser.parseBodyFragment(null, "http://example.com");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testParseBodyFragmentRelaxedWithEmptyHtml() {
        Document doc = Parser.parseBodyFragmentRelaxed("", "http://example.com"); // Empty fragment should return document with empty body
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(0, doc.getElementsByTag("body").size());
    }
}