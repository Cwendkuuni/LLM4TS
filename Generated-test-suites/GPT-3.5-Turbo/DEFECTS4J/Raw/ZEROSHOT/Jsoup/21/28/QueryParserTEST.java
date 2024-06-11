package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParse_WithComma() {
        String query = "div, p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.Or);
    }

    @Test
    public void testParse_WithChildCombinator() {
        String query = "div > p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        Evaluator parentEvaluator = andEvaluator.evaluators().get(0);
        assertTrue(parentEvaluator instanceof StructuralEvaluator.ImmediateParent);
    }

    @Test
    public void testParse_WithDescendantCombinator() {
        String query = "div p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        Evaluator parentEvaluator = andEvaluator.evaluators().get(0);
        assertTrue(parentEvaluator instanceof StructuralEvaluator.Parent);
    }

    @Test
    public void testParse_WithAdjacentSiblingCombinator() {
        String query = "div + p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        Evaluator siblingEvaluator = andEvaluator.evaluators().get(0);
        assertTrue(siblingEvaluator instanceof StructuralEvaluator.ImmediatePreviousSibling);
    }

    @Test
    public void testParse_WithGeneralSiblingCombinator() {
        String query = "div ~ p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        Evaluator siblingEvaluator = andEvaluator.evaluators().get(0);
        assertTrue(siblingEvaluator instanceof StructuralEvaluator.PreviousSibling);
    }

    @Test
    public void testParse_WithId() {
        String query = "#myId";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Id);
    }

    @Test
    public void testParse_WithClass() {
        String query = ".myClass";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Class);
    }

    @Test
    public void testParse_WithTag() {
        String query = "div";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_WithAttribute() {
        String query = "[attr=value]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParse_WithAllElements() {
        String query = "*";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AllElements);
    }

    @Test
    public void testParse_WithIndexLessThan() {
        String query = ":lt(5)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.IndexLessThan);
    }

    @Test
    public void testParse_WithIndexGreaterThan() {
        String query = ":gt(5)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    public void testParse_WithIndexEquals() {
        String query = ":eq(5)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testParse_WithHas() {
        String query = ":has(div)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParse_WithContains() {
        String query = ":contains(text)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.ContainsText);
    }

    @Test
    public void testParse_WithContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testParse_WithMatches() {
        String query = ":matches(regex)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Matches);
    }

    @Test
    public void testParse_WithMatchesOwn() {
        String query = ":matchesOwn(regex)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.MatchesOwn);
    }

    @Test
    public void testParse_WithNot() {
        String query = ":not(div)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof StructuralEvaluator.Not);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithUnknownCombinator() {
        String query = "div&";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithEmptySubselect() {
        String query = ":has()";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithEmptyContains() {
        String query = ":contains()";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithEmptyContainsOwn() {
        String query = ":containsOwn()";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithEmptyMatches() {
        String query = ":matches()";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithEmptyMatchesOwn() {
        String query = ":matchesOwn()";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_WithEmptyNot() {
        String query = ":not()";
        QueryParser.parse(query);
    }

}