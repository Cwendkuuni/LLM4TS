import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test for typical input
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testSetTreeBuilder() {
        // Test for setting the TreeBuilder
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();

        Parser updatedParser = parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, updatedParser.getTreeBuilder());
    }

    // More test cases for other methods should be added here

}