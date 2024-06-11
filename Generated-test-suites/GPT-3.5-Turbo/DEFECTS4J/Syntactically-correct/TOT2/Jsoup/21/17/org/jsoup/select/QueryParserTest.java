import org.jsoup.nodes.Element;
import org.jsoup.select.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Tag);
    }

    @Test
    public void testParseWithCombinators() {
        String query = "div > p + span";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("p > span");
        String subQuery = parser.consumeSubQuery();
        assertEquals("p", subQuery);
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#id");
        parser.byId();
        assertNotNull(parser.evals.get(0));
        assertTrue(parser.evals.get(0) instanceof Evaluator.Id);
    }

    // Additional test cases for other methods can be added

}