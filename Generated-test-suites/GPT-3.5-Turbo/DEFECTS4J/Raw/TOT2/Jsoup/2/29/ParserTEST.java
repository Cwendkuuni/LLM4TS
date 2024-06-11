import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        // Add more assertions here
    }

    // Add more test cases for individual methods

}