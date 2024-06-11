import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testParseRelaxedParsing() {
        String bodyHtml = "<p>Some text</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertFalse(doc.head().hasParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtmlParse() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriParse() {
        Parser.parse("<html></html>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtmlParseBodyFragment() {
        Parser.parseBodyFragment(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriParseBodyFragment() {
        Parser.parseBodyFragment("<div></div>", null);
    }
}