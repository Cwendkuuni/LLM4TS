import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseBasic() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithCombinators() {
        Evaluator evaluator = QueryParser.parse("div > p");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithAttributes() {
        Evaluator evaluator = QueryParser.parse("a[href^=example]");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithPseudoSelectors() {
        Evaluator evaluator = QueryParser.parse(":contains(test)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        try {
            QueryParser.parse("[attr]");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseEmptyString() {
        Evaluator evaluator = QueryParser.parse("");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseInvalidQuery() {
        try {
            QueryParser.parse("div > [attr]");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

}