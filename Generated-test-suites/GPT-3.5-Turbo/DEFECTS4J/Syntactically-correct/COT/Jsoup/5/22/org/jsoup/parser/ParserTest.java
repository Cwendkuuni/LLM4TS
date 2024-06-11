import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Heading", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Heading</h1><p>Paragraph</p>";
        Document doc = Parser.parseBodyFragment(html, "https://www.example.com");
        assertNotNull(doc);
        assertEquals("Heading", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Heading</h1><div><p>Paragraph</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "https://www.example.com");
        assertNotNull(doc);
        assertEquals("Heading", doc.select("h1").text());
        assertEquals("Paragraph", doc.select("p").text());
    }

    // Additional test cases for edge cases and exception handling scenarios:
    @Test
    public void testParseEmpty() {
        String html = "";
        Document doc = Parser.parse(html, "https://www.example.com");
        assertNotNull(doc);
        assertEquals("", doc.title());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test</title></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "https://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        Parser.parse("<html></html>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullHtml() {
        Parser.parseBodyFragment(null, "https://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        Parser.parseBodyFragment("<h1>Heading</h1>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedNullHtml() {
        Parser.parseBodyFragmentRelaxed(null, "https://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxedNullBaseUri() {
        Parser.parseBodyFragmentRelaxed("<h1>Heading</h1>", null);
    }
}