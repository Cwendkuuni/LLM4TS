import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("body", doc.body().tagName());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseComment() {
        // Add test case for parsing comment
        // include edge cases like empty comment, comment with special characters, etc.
    }

    @Test
    public void testParseXmlDecl() {
        // Add test case for parsing XML declaration
        // include edge cases like missing data, invalid characters, etc.
    }

    @Test
    public void testParseEndTag() {
        // Add test case for parsing end tag
        // include edge cases like missing tag, nested tags, etc.
    }

    @Test
    public void testParseStartTag() {
        // Add test case for parsing start tag
        // include edge cases like missing tag, invalid attributes, etc.
    }

    @Test
    public void testParseTextNode() {
        // Add test case for parsing text node
        // include edge cases like empty text, special characters, etc.
    }

    @Test
    public void testParseCdata() {
        // Add test case for parsing CDATA
        // include edge cases like missing closing tag, empty data, etc.
    }

    @Test
    public void testParseAttribute() {
        // Add test case for parsing attribute
        // include edge cases like missing key, missing value, invalid characters, etc.
    }
}