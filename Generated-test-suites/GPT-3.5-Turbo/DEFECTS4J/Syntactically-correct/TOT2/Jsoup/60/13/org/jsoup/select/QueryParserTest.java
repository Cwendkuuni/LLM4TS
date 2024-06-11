import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithCombinators() {
        QueryParser parser = new QueryParser("div > p");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser parser = new QueryParser("invalid");
        parser.parse();
    }

    // Add more test cases for other methods in QueryParser class

}