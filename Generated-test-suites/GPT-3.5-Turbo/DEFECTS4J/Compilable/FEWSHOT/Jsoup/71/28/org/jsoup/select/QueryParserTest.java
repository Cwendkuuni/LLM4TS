package org.jsoup.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);

        evaluator = QueryParser.parse("#id");
        assertEquals(Evaluator.Id.class, evaluator.getClass());

        evaluator = QueryParser.parse(".classname");
        assertEquals(Evaluator.Class.class, evaluator.getClass());

        evaluator = QueryParser.parse("tagname");
        assertEquals(Evaluator.Tag.class, evaluator.getClass());

        evaluator = QueryParser.parse("[attr]");
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());

        evaluator = QueryParser.parse("*");
        assertEquals(Evaluator.AllElements.class, evaluator.getClass());

        evaluator = QueryParser.parse(":lt(2)");
        assertEquals(Evaluator.IndexLessThan.class, evaluator.getClass());

        evaluator = QueryParser.parse(":gt(2)");
        assertEquals(Evaluator.IndexGreaterThan.class, evaluator.getClass());

        evaluator = QueryParser.parse(":eq(2)");
        assertEquals(Evaluator.IndexEquals.class, evaluator.getClass());

        evaluator = QueryParser.parse(":has(selector)");
        assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());

        evaluator = QueryParser.parse(":contains(text)");
        assertEquals(Evaluator.ContainsText.class, evaluator.getClass());

        evaluator = QueryParser.parse(":containsOwn(text)");
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());

        evaluator = QueryParser.parse(":containsData(data)");
        assertEquals(Evaluator.ContainsData.class, evaluator.getClass());

        evaluator = QueryParser.parse(":matches(regex)");
        assertEquals(Evaluator.Matches.class, evaluator.getClass());

        evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());

        evaluator = QueryParser.parse(":not(selector)");
        assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
    }
}