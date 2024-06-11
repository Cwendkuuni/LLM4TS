package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = queryParser.parse("");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser.combinator(',');
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof CombiningEvaluator.Or);

        queryParser.combinator('>');
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof CombiningEvaluator.And);

        queryParser.combinator('+');
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof CombiningEvaluator.And);

        queryParser.combinator('~');
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof CombiningEvaluator.And);
    }

    @Test
    public void testConsumeSubQuery() {
        queryParser.tq = new TokenQueue("a b c");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("a", subQuery);
    }

    @Test
    public void testFindElements() {
        queryParser.tq = new TokenQueue("#id");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Id);

        queryParser.tq = new TokenQueue(".class");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Class);

        queryParser.tq = new TokenQueue("tag");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Tag);

        queryParser.tq = new TokenQueue("[attr]");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Attribute);

        queryParser.tq = new TokenQueue("*");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.AllElements);

        queryParser.tq = new TokenQueue(":lt(1)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexLessThan);

        queryParser.tq = new TokenQueue(":gt(1)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexGreaterThan);

        queryParser.tq = new TokenQueue(":eq(1)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexEquals);

        queryParser.tq = new TokenQueue(":has(div)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof StructuralEvaluator.Has);

        queryParser.tq = new TokenQueue(":contains(text)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.ContainsText);

        queryParser.tq = new TokenQueue(":containsOwn(text)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.ContainsOwnText);

        queryParser.tq = new TokenQueue(":matches(regex)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Matches);

        queryParser.tq = new TokenQueue(":matchesOwn(regex)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.MatchesOwn);

        queryParser.tq = new TokenQueue(":not(selector)");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
        assertTrue(queryParser.evals.get(0) instanceof StructuralEvaluator.Not);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testFindElementsUnhandled() {
        queryParser.tq = new TokenQueue("!@#");
        queryParser.findElements();
    }
}