import org.jsoup.nodes.Document;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_ValidHtml_ReturnsDocumentObject() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.select("title").text());
        assertEquals("Hello World", doc.select("h1").text());
    }
    
    @Test
    public void testParseBaseUri_NullHtml_ThrowsIllegalArgumentException() {
        String baseUri = "http://www.example.com";

        try {
            Document doc = Parser.parse(null, baseUri);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("html must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testParseBaseUri_NullBaseUri_ThrowsIllegalArgumentException() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body>";

        try {
            Document doc = Parser.parse(html, null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragment_ValidHtml_ReturnsDocumentObject() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentBaseUri_NullBodyHtml_ThrowsIllegalArgumentException() {
        String baseUri = "http://www.example.com";

        try {
            Document doc = Parser.parseBodyFragment(null, baseUri);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("bodyHtml must not be null", e.getMessage());
        }
    }

    @Test
    public void testParseBodyFragmentBaseUri_NullBaseUri_ThrowsIllegalArgumentException() {
        String bodyHtml = "<h1>Hello World</h1>";

        try {
            Document doc = Parser.parseBodyFragment(bodyHtml, null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri must not be null", e.getMessage());
        }
    }
}