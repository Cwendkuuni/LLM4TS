import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World!</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("<div><p>Hello World!</p></div>", doc.body().html());
    }

    @Test
    public void testParseInvalidHTML() {
        String html = "<html><head><title>Test</title><body><p>Hello World!</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Exception exception = assertThrows(Exception.class, () -> {
            Parser.parse(html, baseUri);
        });

        assertNotNull(exception);
    }

    // Additional test cases for edge cases and exceptions can be added here

}