package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParse_SingleTag() {
        Evaluator evaluator = QueryParser.parse("p");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_SingleId() {
        Evaluator evaluator = QueryParser.parse("#id");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
    }

    @Test
    public void testParse_SingleClass() {
        Evaluator evaluator = QueryParser.parse(".class");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
    }

    @Test
    public void testParse_SimpleQueries() {
        Evaluator evaluator = QueryParser.parse("p.class#id");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.Class);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(2) instanceof Evaluator.Id);
    }

    @Test
    public void testParse_DescendantSelectors() {
        Evaluator evaluator = QueryParser.parse("p > ul > li");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof StructuralEvaluator.ImmediateParent);
        assertTrue(((StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getEvaluators().get(0)).getChild() instanceof Evaluator.Tag);
        assertTrue(((Evaluator.Tag) ((StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getEvaluators().get(0)).getChild()) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_AttributeSelectors() {
        Evaluator evaluator = QueryParser.parse("p[title]");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testParse_AttributeSelectorsWithValues() {
        Evaluator evaluator = QueryParser.parse("p[title='Hello']");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParse_AttributeSelectorsWithStartingValue() {
        Evaluator evaluator = QueryParser.parse("p[title^='Hello']");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
    }

    @Test
    public void testParse_AttributeSelectorsWithEndingValue() {
        Evaluator evaluator = QueryParser.parse("p[title$='Hello']");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
    }

    @Test
    public void testParse_AttributeSelectorsWithContainingValue() {
        Evaluator evaluator = QueryParser.parse("p[title*='Hello']");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
    }

    @Test
    public void testParse_AttributeSelectorsWithMatchingValue() {
        Evaluator evaluator = QueryParser.parse("p[title~='Hello']");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
    }

    @Test
    public void testParse_PseudoClassSelectors_lt() {
        Evaluator evaluator = QueryParser.parse("p:lt(2)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexLessThan);
        assertEquals(1, ((Evaluator.IndexLessThan) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClassSelectors_gt() {
        Evaluator evaluator = QueryParser.parse("p:gt(2)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);
        assertEquals(3, ((Evaluator.IndexGreaterThan) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClassSelectors_eq() {
        Evaluator evaluator = QueryParser.parse("p:eq(2)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexEquals);
        assertEquals(2, ((Evaluator.IndexEquals) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClassSelectors_has() {
        Evaluator evaluator = QueryParser.parse("p:has(ul)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParse_PseudoClassSelectors_contains() {
        Evaluator evaluator = QueryParser.parse("p:contains(Hello)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
    }

    @Test
    public void testParse_PseudoClassSelectors_containsOwn() {
        Evaluator evaluator = QueryParser.parse("p:containsOwn(Hello)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testParse_PseudoClassSelectors_matches() {
        Evaluator evaluator = QueryParser.parse("p:matches(.foo)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Matches);
    }

    @Test
    public void testParse_PseudoClassSelectors_matchesOwn() {
        Evaluator evaluator = QueryParser.parse("p:matchesOwn(.foo)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
    }

    @Test
    public void testParse_PseudoClassSelectors_not() {
        Evaluator evaluator = QueryParser.parse("p:not(ul)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        QueryParser.parse("p:not(ul) :has(p:not(ul))");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery_unhandled() {
        QueryParser.parse("p:unhandled");
    }
}