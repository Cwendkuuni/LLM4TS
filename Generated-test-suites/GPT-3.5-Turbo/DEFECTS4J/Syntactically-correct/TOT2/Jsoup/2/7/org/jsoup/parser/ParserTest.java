import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        Document doc = Parser.parseBodyFragment("<h1>Hello World</h1>", "example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseComment() {
        // Edge case: Comment with ending "-"
        Parser parser = new Parser("<!-- This is a comment -->", "example.com", false);
        parser.parseComment();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(" This is a comment ", doc.body().text());
    }

    @Test
    public void testParseEndTag() {
        // Edge case: Empty end tag
        Parser parser = new Parser("</>", "example.com", false);
        parser.parseEndTag();
        Document doc = parser.parse();
        assertNotNull(doc);
    }

    @Test
    public void testParseTextNode() {
        // Edge case: Text node containing HTML tags
        Parser parser = new Parser("<p>This is a <b>bold</b> text</p>", "example.com", false);
        parser.parseTextNode();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("This is a bold text", doc.body().text());
    }

    @Test
    public void testParseCdata() {
        // Error scenario: Invalid CDATA
        Parser parser = new Parser("<![CDATA[Invalid CDATA]]>", "example.com", false);
        parser.parseCdata();
        Document doc = parser.parse();
        assertNotNull(doc);
    }

    // Add more test cases for other methods in the Parser class

}