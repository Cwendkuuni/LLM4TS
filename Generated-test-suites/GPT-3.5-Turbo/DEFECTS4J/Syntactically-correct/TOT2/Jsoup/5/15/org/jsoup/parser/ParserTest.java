import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(document);
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(document);
        assertEquals("Hello, World!", document.body().text());
    }

    // Additional test cases for edge cases and exceptions can be added here

}