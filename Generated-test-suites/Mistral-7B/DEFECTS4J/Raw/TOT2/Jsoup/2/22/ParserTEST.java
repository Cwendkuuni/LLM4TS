import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Given a valid HTML string
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        // When parsing the HTML string
        // Then the Document object should be created successfully
        assertNotNull(doc);
        // And the HTML should be parsed correctly
        assertEquals(html, doc.html());
        // And the head and body tags should be present
        assertEquals(htmlTag, doc.head().tag());
        assertEquals(bodyTag, doc.body().tag());

        // Given a malformed HTML string
        html = "<html><body><h1>Test</h1>";

        // When parsing the HTML string
        // Then an exception should be thrown
        try {
            doc = Parser.parse(html, "http://example.com");
            fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}