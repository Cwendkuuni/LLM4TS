import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body></body></html>", "http://example.com");
        assertNotNull(doc);
        
        // Edge case - empty input
        doc = parser.parseInput("", "http://example.com");
        assertNull(doc);
        
        // Exception handling - invalid baseUri
        try {
            doc = parser.parseInput("<html><body></body></html>", "invalid");
            fail("Should throw an exception for invalid baseUri");
        } catch (Exception e) {
            // Expected
        }
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }
    
    // Additional test cases for each public method should be added here
    
}