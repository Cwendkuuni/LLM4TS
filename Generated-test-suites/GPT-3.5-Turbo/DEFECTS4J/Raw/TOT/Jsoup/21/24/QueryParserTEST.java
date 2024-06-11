package org.jsoup.select;

import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {

    @Test
    public void testParse_success() {
        String query = "#id .class[attr='value']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
    }

    @Test
    public void testParse_emptyQuery() {
        String query = "";
        try {
            QueryParser.parse(query);
        } catch (Exception e) {
            assertEquals(Selector.SelectorParseException.class, e.getClass());
            assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_invalidQuery() {
        String query = "#id .class[attr";
        try {
            QueryParser.parse(query);
        } catch (Exception e) {
            assertEquals(Selector.SelectorParseException.class, e.getClass());
            assertEquals("Could not parse attribute query '#id .class[attr': unexpected token at 'class[attr'", e.getMessage());
        }
    }

    @Test
    public void testParse_singleCombinator() {
        String query = "#id > .class + [attr]";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
    }

    @Test
    public void testParse_multipleCombinators() {
        String query = "#id > .class + [attr], #id2 .class2[attr2]";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
    }

    @Test
    public void testParse_multipleCombinators_invalidQuery() {
        String query = "#id > .class + [attr], #id2 .class2[attr2";
        try {
            QueryParser.parse(query);
        } catch (Exception e) {
            assertEquals(Selector.SelectorParseException.class, e.getClass());
            assertEquals("Could not parse attribute query '#id > .class + [attr], #id2 .class2[attr2': unexpected token at 'class2[attr2'", e.getMessage());
        }
    }

    @Test
    public void testParse_containsText() {
        String query = ":contains('text')";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
    }

    @Test
    public void testParse_containsOwnText() {
        String query = ":containsOwn('text')";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
    }

    @Test
    public void testParse_matches() {
        String query = ":matches('regex')";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Matches.class, evaluator.getClass());
    }

    @Test
    public void testParse_matchesOwn() {
        String query = ":matchesOwn('regex')";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
    }

    @Test
    public void testParse_not() {
        String query = ":not(.class)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
    }

    @Test
    public void testParse_indexLessThan() {
        String query = ":lt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IndexLessThan.class, evaluator.getClass());
    }

    @Test
    public void testParse_indexGreaterThan() {
        String query = ":gt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IndexGreaterThan.class, evaluator.getClass());
    }

    @Test
    public void testParse_indexEquals() {
        String query = ":eq(5)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IndexEquals.class, evaluator.getClass());
    }

    @Test
    public void testParse_has() {
        String query = ":has(#id)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
    }

    @Test
    public void testParse_invalidHas() {
        String query = ":has()";
        try {
            QueryParser.parse(query);
        } catch (Exception e) {
            assertEquals(Selector.SelectorParseException.class, e.getClass());
            assertEquals(":has(el) subselect must not be empty", e.getMessage());
        }
    }

    @Test
    public void testParse_unknownCombinator() {
        String query = "#id > .class ~ [attr]";
        try {
            QueryParser.parse(query);
        } catch (Exception e) {
            assertEquals(Selector.SelectorParseException.class, e.getClass());
            assertEquals("Unknown combinator: ~", e.getMessage());
        }
    }
}