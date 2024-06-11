import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case test for parse method
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        // Add more assertions to test the parsed document
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case test for parseBodyFragment method
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        // Add more assertions to test the parsed body fragment
    }

    @Test
    public void testParseWithRelaxedParsing() {
        // Edge case test for parse method with relaxed parsing enabled
        String html = "<ul><li>Item 1<li>Item 2";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        // Add more assertions to test the parsed document with relaxed parsing
    }

    @Test
    public void testInvalidHtml() {
        // Error scenario test for parse method with invalid HTML
        String html = "<html><body><h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNull(doc);
        // Add more assertions to test error handling for invalid HTML
    }

    // Add more test cases for other methods in the Parser class

}