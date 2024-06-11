import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        // Add more assertions here for expected results
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        // Add more assertions here for expected results
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical use case
        String bodyHtml = "<div><p>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        // Add more assertions here for expected results
    }

    // Add more test cases for other methods in the Parser class

}