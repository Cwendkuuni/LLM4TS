import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.test";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        // add more assertions here based on the expected behavior of the parse method
    }

    @Test
    public void testParse_InvalidQuery() {
        String query = "div#id.class[attr]";
        try {
            QueryParser.parse(query);
            fail("Should throw SelectorParseException for invalid query");
        } catch (Selector.SelectorParseException e) {
            // expected exception
        }
    }

    @Test
    public void testCombinator() {
        QueryParser qParser = new QueryParser("div > p");
        String subQuery = qParser.consumeSubQuery();
        assertEquals("p", subQuery);
        // add more assertions here based on the expected behavior of the combinator method
    }

    // add more test cases for each method in QueryParser class

}