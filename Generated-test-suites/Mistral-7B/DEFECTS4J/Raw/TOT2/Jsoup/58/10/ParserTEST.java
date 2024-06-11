import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        Document doc = parser.parseInput(html, baseUri);

        // Then
        assertEquals("Hello World", doc.select("h1").text());
        assertNull(parser.getErrors().get(0).getMessage());

        // Edge Case: Empty HTML
        html = "";
        parser = new Parser(new HtmlTreeBuilder());
        doc = parser.parseInput(html, baseUri);
        assertEquals(0, parser.getErrors().size());

        // Edge Case: HTML with errors
        html = "<html><body><h1><script>alert('XSS')</script></h1></body></html>";
        ParseErrorList errors = ParseErrorList.tracking(1);
        parser = new Parser(new HtmlTreeBuilder());
        doc = parser.parseInput(html, baseUri, null, null);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getMessage().contains("script tag"));
    }

    // Test other methods here...
}