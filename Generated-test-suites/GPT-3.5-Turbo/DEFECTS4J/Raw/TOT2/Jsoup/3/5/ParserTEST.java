import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document parsedDoc = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDoc);
        assertEquals("http://example.com", parsedDoc.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document parsedBody = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(parsedBody);
        assertEquals("http://example.com", parsedBody.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document parsedBody = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(parsedBody);
        assertEquals("http://example.com", parsedBody.baseUri());
        // Add more assertions as needed
    }

    // Add more test cases for edge cases and exceptions as needed

}