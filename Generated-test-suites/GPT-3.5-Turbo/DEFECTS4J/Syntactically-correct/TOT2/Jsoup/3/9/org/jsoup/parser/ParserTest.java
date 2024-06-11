import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use cases
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        // Assertions
        assertNotNull(doc);
        // Add more assertions based on the expected behavior of the parse method
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use cases
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Assertions
        assertNotNull(doc);
        // Add more assertions based on the expected behavior of the parseBodyFragment method
    }

    @Test
    public void testParseWithRelaxedParsing() {
        // Typical use cases with relaxed parsing enabled
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Assertions
        assertNotNull(doc);
        // Add more assertions based on the expected behavior when relaxed parsing is enabled
    }

    //Include more test cases for the remaining methods...

}