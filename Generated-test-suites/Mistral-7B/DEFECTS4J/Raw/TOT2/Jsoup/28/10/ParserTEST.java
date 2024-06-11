import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        List<ParseError> errors = parser.getErrors();
        Document doc = parser.parseInput(html, baseUri);

        // Then
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
        assertEmpty(errors);
    }

    @Test
    public void testParseInputWithErrors() {
        // Given
        String html = "<script>alert('Hello');</script>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        // When
        List<ParseError> errors = parser.getErrors();

        // Then
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected '<script>', expecting '<!DOCTYPE'", error.getMessage());
    }

    // Add other test cases for other methods here...
}