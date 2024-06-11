import org.jsoup.select.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class > a[href*=example.com]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        // Add more test cases for parse method here
    }

    @Test
    public void testCombiningEvaluator() {
        QueryParser queryParser = new QueryParser("div.class > a[href*=example.com]");
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        // Add more test cases for combinator method here
    }

    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("div");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Tag);
        // Add more test cases for byTag method here
    }

    // Add more test cases for other public methods in QueryParser class

}