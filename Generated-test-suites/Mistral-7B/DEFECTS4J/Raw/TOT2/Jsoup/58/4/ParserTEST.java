// ParserTest.java (Expert 1)

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("invalid input", "");
        fail("Expected IllegalArgumentException");
    }

    // Add test cases for other methods and potential edge cases here
}


// Add  after all test cases