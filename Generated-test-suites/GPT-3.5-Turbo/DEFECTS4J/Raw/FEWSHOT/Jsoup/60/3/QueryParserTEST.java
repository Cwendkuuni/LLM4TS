package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case 1
        Evaluator result1 = QueryParser.parse("div#test");
        assertNotNull(result1);

        // Test case 2
        Evaluator result2 = QueryParser.parse("div.class");
        assertNotNull(result2);

        // Test case 3
        Evaluator result3 = QueryParser.parse("div[attr=value]");
        assertNotNull(result3);

        // Test case 4
        Evaluator result4 = QueryParser.parse("div:has(span)");
        assertNotNull(result4);

        // Test case 5
        Evaluator result5 = QueryParser.parse(":contains(text)");
        assertNotNull(result5);
    }

    @Test
    public void testParse_EvaluatorNotNull() {
        Evaluator result = QueryParser.parse("div#test");
        assertNotNull(result);
    }

    @Test
    public void testParse_EvaluatorOfTypeStructuralEvaluator() {
        Evaluator result = QueryParser.parse("div[attr=value]");
        assertTrue(result instanceof StructuralEvaluator);
    }

    @Test
    public void testParse_EvaluatorOfTypeEvaluator() {
        Evaluator result = QueryParser.parse("div.class");
        assertTrue(result instanceof Evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        QueryParser.parse(":invalid");
    }
}