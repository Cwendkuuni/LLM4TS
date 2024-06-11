import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseEdgeCases() {
        // Empty HTML
        String html = "";
        String baseUri = "http://example.com";
        Document docEmptyHtml = Parser.parse(html, baseUri);
        assertNull(docEmptyHtml);
        
        // Null baseUri
        String htmlNullBaseUri = "<html></html>";
        String baseUriNull = null;
        Document docNullBaseUri = Parser.parse(htmlNullBaseUri, baseUriNull);
        assertNull(docNullBaseUri);
        
        // Invalid HTML input
        String htmlInvalid = "<html><head><title></title></head><body></bo></html>";
        String baseUriInvalid = "http://example.com";
        Document docInvalid = Parser.parse(htmlInvalid, baseUriInvalid);
        assertNull(docInvalid);
    }
}