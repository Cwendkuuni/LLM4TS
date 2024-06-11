import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_WithValidHtml_ReturnsDocumentObject() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull("Document object should not be null", doc);
        assertEquals("Document title should be 'Test'", "Test", doc.title());
        assertEquals("Document base URI should be 'http://www.example.com'", "http://www.example.com", doc.baseUri());
        assertEquals("Document body should contain a single h1 element", 1, doc.body().getElementsByTag("h1").size());
        assertEquals("Document body h1 element should contain text 'Hello World'", "Hello World", doc.body().getElementsByTag("h1").first().text());
    }

    @Test
    public void testParse_WithEmptyHtml_ReturnsEmptyDocument() {
        String html = "";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull("Document object should not be null", doc);
        assertEquals("Document title should be empty", "", doc.title());
        assertEquals("Document base URI should be 'http://www.example.com'", "http://www.example.com", doc.baseUri());
        assertEquals("Document body should be empty", 0, doc.body().children().size());
    }

    @Test
    public void testParseBodyFragment_WithValidFragment_ReturnsDocumentObjectWithFragmentAsBody() {
        String fragment = "<h1>Hello World</h1>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(fragment, baseUri);

        assertNotNull("Document object should not be null", doc);
        assertEquals("Document title should be empty", "", doc.title());
        assertEquals("Document base URI should be 'http://www.example.com'", "http://www.example.com", doc.baseUri());
        assertEquals("Document body should contain a single h1 element", 1, doc.body().getElementsByTag("h1").size());
        assertEquals("Document body h1 element should contain text 'Hello World'", "Hello World", doc.body().getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseBodyFragment_WithEmptyFragment_ReturnsDocumentObjectWithEmptyBody() {
        String fragment = "";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(fragment, baseUri);

        assertNotNull("Document object should not be null", doc);
        assertEquals("Document title should be empty", "", doc.title());
        assertEquals("Document base URI should be 'http://www.example.com'", "http://www.example.com", doc.baseUri());
        assertEquals("Document body should be empty", 0, doc.body().children().size());
    }

    @Test
    public void testParse_WithInvalidHtml_ReturnsDocumentObjectWithErrors() {
        String html = "<h1>Invalid HTML";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull("Document object should not be null", doc);
        assertTrue("Document should contain parse errors", doc.hasParseErrors());
        assertEquals("Document parse errors should contain one entry", 1, doc.parseErrors().size());
        assertEquals("Document parse error should have expected error message", "Unexpected end tag (h1)", doc.parseErrors().get(0).getErrorMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithNullHtml_ThrowsIllegalArgumentException() {
        String html = null;
        String baseUri = "http://www.example.com";

        Parser.parse(html, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithNullBaseUri_ThrowsIllegalArgumentException() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = null;

        Parser.parse(html, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_WithNullFragment_ThrowsIllegalArgumentException() {
        String fragment = null;
        String baseUri = "http://www.example.com";

        Parser.parseBodyFragment(fragment, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_WithNullBaseUri_ThrowsIllegalArgumentException() {
        String fragment = "<h1>Hello World</h1>";
        String baseUri = null;

        Parser.parseBodyFragment(fragment, baseUri);
    }

}