import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Basic test case
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        assert(doc.body().text().equals("Hello World"));

        // Edge cases
        String htmlWithScript = "<html><body onload=\"alert('Hello')\"><script>alert('World')</script></body></html>";
        Parser parserWithTrackErrors = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        List<ParseError> errors = parserWithTrackErrors.parseInput(htmlWithScript, "http://example.com").getErrors();
        assert(errors.size() == 1);
        assert(errors.get(0).getMessage().contains("Scripting is disabled"));

        String htmlWithErrors = "<html><body><script>throw new Error('parse error')</script></body></html>";
        Parser parserWithMaxErrors = new Parser(new HtmlTreeBuilder()).setTrackErrors(2);
        errors = parserWithMaxErrors.parseInput(htmlWithErrors, "http://example.com").getErrors();
        assert(errors.size() == 2);
        assert(errors.get(1).getMessage().contains("parse error"));

        // Exception handling
        String invalidHtml = "<html><body><script>throw new Exception('parse exception')</script></body></html>";
        try {
            parserWithMaxErrors.parseInput(invalidHtml, "http://example.com");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}