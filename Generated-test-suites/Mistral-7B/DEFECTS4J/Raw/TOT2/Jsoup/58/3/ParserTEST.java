import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParseInput_ValidHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertNotNull(document);
        Element h1 = document.body().getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseInput_MaxErrorsReached() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = null;
        try {
            document = parser.parseInput("<html><body invalid></body></html>", "http://example.com");
        } catch (Exception e) {
            List<ParseError> errors = parser.getErrors();
            assertEquals(1, errors.size());
            assertEquals("Error message", errors.get(0).getMessage());
        }
        assertNull(document);
    }

    // Add test cases for other methods here...
}