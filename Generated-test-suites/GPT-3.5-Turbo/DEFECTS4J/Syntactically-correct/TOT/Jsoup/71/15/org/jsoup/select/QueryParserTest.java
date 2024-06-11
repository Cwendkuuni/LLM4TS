import org.jsoup.select.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    // Test cases for the parse() method

    @Test
    public void testParse_withValidQuery_shouldReturnEvaluator() {
        String query = "div.className";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_withInvalidQuery_shouldThrowException() {
        String query = "div[";
        QueryParser.parse(query);
    }

    // Test cases for the combinator() method

    @Test
    public void testCombinator_withValidCombinator_shouldUpdateCurrentEval() {
        QueryParser parser = new QueryParser("div.className");
        parser.combinator('+');

        Evaluator currentEval = parser.evals.get(0);
        assertTrue(currentEval instanceof CombiningEvaluator.And);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testCombinator_withInvalidCombinator_shouldThrowException() {
        QueryParser parser = new QueryParser("div.className");
        parser.combinator('#');
    }

    // Test cases for the consumeSubQuery() method

    @Test
    public void testConsumeSubQuery_withValidSubQuery_shouldReturnSubQuery() {
        QueryParser parser = new QueryParser("div.className");
        String subQuery = parser.consumeSubQuery();

        assertEquals("div.className", subQuery);
    }

    @Test
    public void testConsumeSubQuery_withParenthesesInSubQuery_shouldReturnSubQueryWithParentheses() {
        QueryParser parser = new QueryParser("div:not(:first-child)");
        String subQuery = parser.consumeSubQuery();

        assertEquals("div(not(:first-child))", subQuery);
    }

    // Test cases for the findElements() method

    @Test
    public void testFindElements_withIdSelector_shouldAddIdEvaluatorToList() {
        QueryParser parser = new QueryParser("#myId");
        parser.findElements();

        Evaluator idEvaluator = parser.evals.get(0);
        assertTrue(idEvaluator instanceof Evaluator.Id);
    }

    @Test
    public void testFindElements_withClassSelector_shouldAddClassEvaluatorToList() {
        QueryParser parser = new QueryParser(".myClass");
        parser.findElements();

        Evaluator classEvaluator = parser.evals.get(0);
        assertTrue(classEvaluator instanceof Evaluator.Class);
    }

    // Additional test cases for other methods...

}