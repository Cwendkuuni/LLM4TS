import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Test for basic functionality
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().select("h1").text());

        // Test for edge cases and exception handling
        String invalidHtml = "<html><head><title>Test</title><!--comment--></body></html>";
        parser = new Parser(invalidHtml, baseUri, false);
        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected exception
        }

        String bodyFragmentHtml = "<body><h1>Hello World</h1></body>";
        Document fragmentDoc = Parser.parseBodyFragment(bodyFragmentHtml, baseUri);
        assertEquals("Hello World", fragmentDoc.body().select("h1").text());
    }
}