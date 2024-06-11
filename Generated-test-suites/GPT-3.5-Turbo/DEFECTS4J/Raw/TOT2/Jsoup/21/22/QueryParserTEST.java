import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector.SelectorParseException;
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
    public void testParseWithCombinator() {
        String query = "div + span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMultipleCombinators() {
        String query = "div > span + a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMultipleSelectors() {
        String query = "div, span, a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "div [id =]";
        try {
            QueryParser.parse(query);
            fail("Should have thrown SelectorParseException");
        } catch (SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseWithNoEvaluator() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }
}