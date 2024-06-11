package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
    }

    @Test
    public void testParse_withId() {
        Evaluator evaluator = QueryParser.parse("#testId");
        assertEquals(Evaluator.Id.class, evaluator.getClass());
    }

    @Test
    public void testParse_withClass() {
        Evaluator evaluator = QueryParser.parse(".testClass");
        assertEquals(Evaluator.Class.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttribute() {
        Evaluator evaluator = QueryParser.parse("[name]");
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttributeWithValue() {
        Evaluator evaluator = QueryParser.parse("[name=value]");
        assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttributeWithValueNot() {
        Evaluator evaluator = QueryParser.parse("[name!=value]");
        assertEquals(Evaluator.AttributeWithValueNot.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttributeWithValueStarting() {
        Evaluator evaluator = QueryParser.parse("[name^=value]");
        assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttributeWithValueEnding() {
        Evaluator evaluator = QueryParser.parse("[name$=value]");
        assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttributeWithValueContaining() {
        Evaluator evaluator = QueryParser.parse("[name*=value]");
        assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
    }

    @Test
    public void testParse_withAttributeWithValueMatching() {
        Evaluator evaluator = QueryParser.parse("[name~=value]");
        assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
    }

    @Test
    public void testParse_withIndexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(10)");
        assertEquals(Evaluator.IndexLessThan.class, evaluator.getClass());
    }

    @Test
    public void testParse_withIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(5)");
        assertEquals(Evaluator.IndexGreaterThan.class, evaluator.getClass());
    }

    @Test
    public void testParse_withIndexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(2)");
        assertEquals(Evaluator.IndexEquals.class, evaluator.getClass());
    }

    @Test
    public void testParse_withHas() {
        Evaluator evaluator = QueryParser.parse(":has(div)");
        assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
    }

    @Test
    public void testParse_withContains() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
    }

    @Test
    public void testParse_withContainsOwn() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(text)");
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
    }

    @Test
    public void testParse_withMatches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertEquals(Evaluator.Matches.class, evaluator.getClass());
    }

    @Test
    public void testParse_withMatchesOwn() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
    }

    @Test
    public void testParse_withNot() {
        Evaluator evaluator = QueryParser.parse(":not(div)");
        assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
    }
}