import static org.junit.Assert.*;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.test";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_emptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_combinator() {
        String query = "div.test > span";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_attribute() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_pseudo() {
        String query = ":contains('text')";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_not() {
        String query = ":not(div.test)";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_complexQuery() {
        String query = "div.test > span, :not(a.link)";
        Evaluator evaluator = QueryParser.parse(query);
        // assert statements
        assertNotNull(evaluator);
    }
}