import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Basic Test: Parse a valid HTML string
        String htmlString = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(htmlString, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        assertEquals("Test Title", root.head().title().text());
        assertEquals("Test Heading", root.getElementsByTag("h1").text());
    }

    @Test
    public void testParseException1() {
        // Edge Case: Parse an empty HTML string
        Parser parser = new Parser("", "http://example.com");
        try {
            parser.parse();
            fail("Expected an IllegalArgumentException as the given HTML string is empty.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseException2() {
        // Edge Case: Parse an HTML string with missing start tag
        String htmlString = "<title>Test Title</title";
        Parser parser = new Parser(htmlString, "http://example.com");
        try {
            parser.parse();
            fail("Expected a ParseException as the given HTML string is invalid.");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseException3() {
        // Error Case: Parse an HTML string with a tag that does not have a valid parent
        String htmlString = "<head><title><body>";
        Parser parser = new Parser(htmlString, "http://example.com");
        try {
            parser.parse();
            fail("Expected a ParseException as the given HTML string contains an invalid tag sequence.");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    // Add more test cases for other methods in the Parser class
}