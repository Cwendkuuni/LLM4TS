import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Check the document structure
        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        Element headNode = htmlNode.getFirstChild();
        assertEquals("head", headNode.tagName());
        Element titleNode = headNode.getFirstChild();
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Page", titleNode.text());

        Element bodyNode = htmlNode.getNextSibling();
        assertEquals("body", bodyNode.tagName());
        Element h1Node = bodyNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Fragment</h1></body>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        // Check the document structure
        Element htmlNode = doc;
        Element bodyNode = htmlNode.getFirstChild();
        assertEquals("body", bodyNode.tagName());
        Element h1Node = bodyNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Fragment", h1Node.text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World<script>alert('XSS')</script></h1></body></html>";
        String baseUri = "http://test.com";

        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected
        }
    }
}