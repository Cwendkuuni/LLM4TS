import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        // Add more assertions to validate the functionality of parse method
    }
    
    @Test
    public void testParseWithInvalidQuery() {
        String query = "div && p"; // Invalid query
        try {
            QueryParser.parse(query);
            fail("IllegalArgumentException expected");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCombinatorGreaterThan() {
        QueryParser queryParser = new QueryParser("div > p");
        queryParser.parse();
        // Add assertions for combinator method with '>'
    }
    
    // Add more test cases for other public methods and edge cases
    
}