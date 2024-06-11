import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseMethod() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body><div>Content</div></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        // Test if the title tag is parsed correctly
        assertEquals("Test", doc.title());

        // Edge case: Empty HTML
        String emptyHtml = "";
        Document emptyDoc = Parser.parse(emptyHtml, baseUri);
        assertNotNull(emptyDoc);
        // Test if the empty document is created
        assertEquals("", emptyDoc.title());
    }

    @Test
    public void testParseBodyFragmentMethod() {
        // Typical use case
        String bodyHtml = "<div>Content</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        // Test if the body tag is parsed correctly
        assertEquals("Content", doc.body().text());

        // Edge case: Empty fragment
        String emptyBodyHtml = "";
        Document emptyDoc = Parser.parseBodyFragment(emptyBodyHtml, baseUri);
        assertNotNull(emptyDoc);
        // Test if the empty body is created
        assertEquals("", emptyDoc.body().text());
    }

    @Test
    public void testParseComment() {
        // Typical use case
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseComment();
        // Test if the comment is appended as a child to the last element
        assertEquals(1, parser.last().childNodes().size());
        assertTrue(parser.last().childNode(0) instanceof Comment);

        // Edge case: Comment with special characters
        Parser parser2 = new Parser("", "http://example.com", false);
        parser2.parseComment();
        // Test if the comment is parsed correctly
        Comment comment = (Comment) parser2.last().childNode(0);
        assertEquals("This is a <!-- test --> comment", comment.getData());
    }

    // More test cases for other methods...

}