import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseInvalidQuery() {
        String query = "div >> span"; // Invalid query with double '>'
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div span");
        parser.parse();
        // Add assertion here
    }

    // Add more test cases for other methods in QueryParser class

}