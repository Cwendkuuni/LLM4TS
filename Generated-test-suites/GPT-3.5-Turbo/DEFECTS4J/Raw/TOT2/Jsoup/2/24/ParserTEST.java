import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Test parsing of basic HTML structure
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Test parsing of HTML fragment into body
        assertNotNull(doc);
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
    }

    // Add more test cases for other methods in the Parser class

}