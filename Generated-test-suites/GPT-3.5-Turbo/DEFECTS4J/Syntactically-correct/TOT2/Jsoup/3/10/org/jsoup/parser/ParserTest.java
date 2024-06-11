import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    // Test case for parse method
    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    // Test case for parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String html = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    // Test case for parse method with XML declaration
    @Test
    public void testParseWithXmlDeclaration() {
        String html = "<?xml version='1.0' encoding='UTF-8'?><html></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("1.0", doc.childNode(0).toString());
    }

    // Test case for parse method with comment
    @Test
    public void testParseWithComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals(" This is a comment ", doc.childNode(0).outerHtml());
    }

    // Test case for parse method with CDATA
    @Test
    public void testParseWithCdata() {
        String html = "<![CDATA[<html></html>]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html></html>", doc.childNode(0).outerHtml());
    }

    // Test case for error handling in parse method
    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        String baseUri = "http://example.com";
        Parser.parse(null, baseUri);
    }

    // Test case for error handling in parse method
    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html></html>";
        Parser.parse(html, null);
    }

    // Add more test cases for additional methods as needed

}