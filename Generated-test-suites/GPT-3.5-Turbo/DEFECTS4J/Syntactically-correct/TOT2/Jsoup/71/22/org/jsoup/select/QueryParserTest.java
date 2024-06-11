import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class > p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "";
        try {
            QueryParser.parse(query);
            fail("Invalid query should throw IllegalArgumentException");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("div.class > p");
        queryParser.parse();
        // Add assertions for combinator method
    }

    // Add more test cases for other methods in QueryParser class

}