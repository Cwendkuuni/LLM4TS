import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String query = "div.test";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);

        // Edge case
        query = "*";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);

        // Exception handling
        try {
            QueryParser.parse(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for other methods in QueryParser class

}