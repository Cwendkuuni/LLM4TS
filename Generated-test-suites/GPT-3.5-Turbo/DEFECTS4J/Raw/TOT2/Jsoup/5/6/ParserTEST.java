import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // typical use case
        String html = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        // add assertions here
    }

    @Test
    public void testParseBodyFragment() {
        // typical use case
        String bodyHtml = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
        // add assertions here
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // typical use case
        String bodyHtml = "<p>Hello, world!</p>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        // add assertions here
    }

    // Add more test cases for edge cases and exception handling scenarios

}