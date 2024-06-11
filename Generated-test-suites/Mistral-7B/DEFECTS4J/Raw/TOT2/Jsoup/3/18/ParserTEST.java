import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParser() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Check document structure
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals(1, doc.numHeaders());
        assertEquals(1, doc.select("h1").size());

        // Check base URI
        assertEquals(baseUri, doc.baseUri());

        // Check parseComment and parseXmlDecl
        parser = new Parser("<html><!-- This is a comment --><!-- and so is this -->" +
                "<!--<![CDATA[ <![CDATA[ This is cdata ]]> ]]>-->" +
                "<!-- <?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "</html>", baseUri, false);
        doc = parser.parse();
        assertEquals(2, doc.children().size());
        assertTrue(doc.child(0) instanceof Comment);
        assertTrue(doc.child(1) instanceof XmlDeclaration);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a body fragment.</p>";
        String baseUri = "http://test.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(1, doc.numHeaders());
        assertEquals(1, doc.body().children().size());
        assertEquals("This is a body fragment.", doc.select("p").text());
    }

    @Test
    public void testParseWithEmptyInput() {
        String html = "";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        try {
            parser.parse();
            fail("ExpectedIllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithMalformedInput() {
        String html = "<html><body></body><body>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        try {
            parser.parse();
            fail("ExpectedIllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for parseEndTag, parseStartTag, parseAttribute, parseTextNode, and parseCdata
}