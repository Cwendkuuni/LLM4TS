import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private String html;
    private String baseUri;

    @Before
    public void setup() {
        html = "<html><body><h1>Test HTML</h1></body></html>";
        baseUri = "http://example.com";
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test HTML</h1>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test HTML</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test HTML</h1>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test HTML</h1>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test HTML</h1>\n </body>\n</html>", doc.toString());
    }

    @Test
    public void testParse_withEmptyHtml() {
        Document doc = Parser.parse("", baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withNullHtml() {
        Parser.parse(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withNullBaseUri() {
        Parser.parse(html, null);
    }

    @Test
    public void testParseBodyFragment_withEmptyBodyHtml() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed_withEmptyBodyHtml() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed_withMissingTag() {
        String bodyHtml = "<h1>Test HTML";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Test HTML</h1>\n </body>\n</html>", doc.toString());
    }

    // Add more test cases to cover other methods, edge cases, and exception handling scenarios

    // 
    // All the test cases
    // 
}