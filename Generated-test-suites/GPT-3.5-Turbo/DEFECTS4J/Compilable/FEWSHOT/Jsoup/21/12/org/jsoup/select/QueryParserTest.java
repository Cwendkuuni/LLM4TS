package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);

        query = "div.myClass";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);

        query = "div";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);

        query = "div[attr=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);

        query = "div[attr^=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeStarting);

        query = "div[attr=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);

        query = "div[attr!=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);

        query = "div[attr*=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);

        query = "div[attr~=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);

        query = "div, span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);

        query = "div > span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        query = "div + span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        query = "div ~ span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        query = "div:lt(2)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexLessThan);

        query = "div:gt(2)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);

        query = "div:eq(2)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexEquals);

        query = "div:has(span)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);

        query = "div:contains(text)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);

        query = "div:containsOwn(text)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);

        query = "div:matches(regex)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Matches);

        query = "div:matchesOwn(regex)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);

        query = "div:not(span)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }
}