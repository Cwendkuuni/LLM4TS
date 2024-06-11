import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {
    @Test
    public void testParseSimpleQuery() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        //Assertions for evaluator
    }

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException for empty query");
        } catch (Selector.SelectorParseException e) {
            //Expected
        }
    }

    @Test
    public void testParseInvalidQuery() {
        String query = "div% class1";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException for invalid query");
        } catch (Selector.SelectorParseException e) {
            //Expected
        }
    }
}