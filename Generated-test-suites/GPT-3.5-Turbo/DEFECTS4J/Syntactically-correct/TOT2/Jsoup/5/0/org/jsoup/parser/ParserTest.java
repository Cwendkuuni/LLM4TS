import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Body fragment</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Body fragment</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><head><title></head></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNull(doc); // Expecting null due to invalid HTML structure
    }

    @Test
    public void testParseBodyFragmentWithMissingTags() {
        String bodyHtml = "Body fragment without closing tag";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        // Add further assertions here for different edge cases
    }

    @Test
    public void testParseBodyFragmentRelaxedWithSpecialCharacters() {
        String bodyHtml = "<p>Body fragment with special characters &amp; &lt; &gt;</p>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        // Add further assertions here for different edge cases including special characters
    }
}