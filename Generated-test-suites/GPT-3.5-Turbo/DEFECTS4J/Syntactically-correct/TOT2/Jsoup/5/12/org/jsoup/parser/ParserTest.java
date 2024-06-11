import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Document parsedDocument = Parser.parse(html, baseUri);
        
        assertNotNull(parsedDocument);
        assertEquals("Test", parsedDocument.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, Body!</p>";
        String baseUri = "http://example.com";
        Document parsedDocument = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(parsedDocument);
        assertEquals("Hello, Body!", parsedDocument.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><span>Relaxed</div>";
        String baseUri = "http://example.com";
        Document parsedDocument = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(parsedDocument);
        assertEquals("<div><span>Relaxed</div>", parsedDocument.body().html());
    }

    // Add more test cases for edge cases and exception handling scenarios here

}