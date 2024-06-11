import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1 > div.class2, p.class3 ~ p.class4";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add assertions for the expected structure of evaluator tree here.
    }

    @Test
    public void testCombinator_GreaterThan() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add assertions for the expected structure of evaluator tree here.
    }

    @Test
    public void testCombinator_Space() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add assertions for the expected structure of evaluator tree here.
    }

    @Test
    public void testCombinator_Plus() {
        String query = "div + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add assertions for the expected structure of evaluator tree here.
    }

    @Test
    public void testCombinator_Tilde() {
        String query = "div ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add assertions for the expected structure of evaluator tree here.
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testUnknownCombinator() {
        String query = "div @> p"; // Unknown combinator symbol "@>"
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }
}