import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertEquals("Hello World!", doc.select("h1").first().text());
    }
    
    // Add additional test methods for other methods in the Parser class
    
}