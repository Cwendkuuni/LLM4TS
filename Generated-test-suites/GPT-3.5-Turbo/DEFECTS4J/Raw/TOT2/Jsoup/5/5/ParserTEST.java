import org.jsoup.nodes.Document;
import org.junit.Test;
import org.jsoup.helper.Validate;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        // Check if HTML elements are parsed correctly
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Check if body fragment is parsed correctly
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        // Check if body fragment with relaxed parsing is parsed correctly
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    // Add more test cases for edge cases and error scenarios as needed

}