package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        assertEquals(new Evaluator.AllElements(), QueryParser.parse("*"));
        assertEquals(new Evaluator.Tag("div"), QueryParser.parse("div"));
        assertEquals(new Evaluator.Tag("h1"), QueryParser.parse("h1"));
        assertEquals(new Evaluator.Id("myId"), QueryParser.parse("#myId"));
        assertEquals(new Evaluator.Class("myClass"), QueryParser.parse(".myClass"));
        assertEquals(new Evaluator.Attribute("myAttr"), QueryParser.parse("[myAttr]"));
        assertEquals(new Evaluator.AttributeStarting("myAttr"), QueryParser.parse("[^myAttr]"));
        assertEquals(new Evaluator.AttributeWithValue("myAttr", "myValue"), QueryParser.parse("[myAttr=myValue]"));
        assertEquals(new Evaluator.AttributeWithValueNot("myAttr", "myValue"), QueryParser.parse("[myAttr!=myValue]"));
        assertEquals(new Evaluator.AttributeWithValueStarting("myAttr", "myValue"), QueryParser.parse("[myAttr^=myValue]"));
        assertEquals(new Evaluator.AttributeWithValueEnding("myAttr", "myValue"), QueryParser.parse("[myAttr$=myValue]"));
        assertEquals(new Evaluator.AttributeWithValueContaining("myAttr", "myValue"), QueryParser.parse("[myAttr*=myValue]"));
        assertEquals(new Evaluator.AttributeWithValueMatching("myAttr", Pattern.compile("myValue")), QueryParser.parse("[myAttr~=myValue]"));
        assertEquals(new Evaluator.IndexLessThan(10), QueryParser.parse(":lt(10)"));
        assertEquals(new Evaluator.IndexGreaterThan(5), QueryParser.parse(":gt(5)"));
        assertEquals(new Evaluator.IndexEquals(2), QueryParser.parse(":eq(2)"));
        assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("span")), QueryParser.parse(":has(span)"));
        assertEquals(new Evaluator.ContainsText("hello"), QueryParser.parse(":contains(hello)"));
        assertEquals(new Evaluator.ContainsOwnText("world"), QueryParser.parse(":containsOwn(world)"));
        assertEquals(new Evaluator.Matches(Pattern.compile("regex")), QueryParser.parse(":matches(regex)"));
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile("regex")), QueryParser.parse(":matchesOwn(regex)"));
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("a")), QueryParser.parse(":not(a)"));
    }
}