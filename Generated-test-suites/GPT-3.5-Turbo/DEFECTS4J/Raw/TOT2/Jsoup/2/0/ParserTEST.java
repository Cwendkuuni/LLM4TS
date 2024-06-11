import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Element body = doc.body();
        assertEquals("body", body.tagName());

        // Edge case: Test for parsing complex HTML with nested elements
        html = "<html><body><div><span><a href='https://example.com'>Example</a></span></div></body></html>";
        doc = Parser.parse(html, baseUri);
        Element a = doc.select("a").first();
        assertEquals("a", a.tagName());
        assertEquals("Example", a.text());
        assertEquals("https://example.com", a.attr("href"));
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element h1 = doc.select("h1").first();
        assertEquals("h1", h1.tagName());

        // Edge case: Test for parsing empty body fragment
        bodyHtml = "";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(0, doc.children().size());
    }

    @Test
    public void testParseComment() {
        // Typical use case
        String html = "<!-- This is a comment -->";
        Parser parser = new Parser(html, "http://example.com", false);
        parser.parseComment();
        Element element = parser.last();
        assertEquals("<!-- This is a comment -->", element.toString());
        
        // Edge case: Test for parsing comment with special characters
        html = "<!-- Special characters & * % -->";
        parser = new Parser(html, "http://example.com", false);
        parser.parseComment();
        element = parser.last();
        assertEquals("<!-- Special characters & * % -->", element.toString());
    }

    // Add more test cases for other methods in Parser class

}