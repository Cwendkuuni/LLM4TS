import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div>Hello World!</div></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals(1, document.select("div").size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello</div>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(document);
        assertEquals(1, document.select("div").size());
    }

    @Test
    public void testParseWithRelaxedParsing() {
        // Add test case for parse with relaxed parsing
    }
}