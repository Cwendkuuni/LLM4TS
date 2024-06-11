package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseIdQuery() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseClassQuery() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        String query = "[href='http://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeWithValueStartingQuery() {
        String query = "[href^='http']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeWithValueEndingQuery() {
        String query = "[href$='.pdf']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeWithValueContainingQuery() {
        String query = "[href*='example']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeWithValueMatchingQuery() {
        String query = "[href~='js']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseCombinedQuery() {
        String query = "div.myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(evaluator.toString(), query);

        Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).getChild(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals(tagEvaluator.toString(), "div");

        Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getChild(1);
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals(classEvaluator.toString(), ".myClass");
    }

    @Test
    public void testParseNegatedQuery() {
        String query = ":not(.myClass)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals(evaluator.toString(), query);

        Evaluator notClassEvaluator = ((StructuralEvaluator.Not) evaluator).getChild(0);
        assertTrue(notClassEvaluator instanceof Evaluator.Class);
        assertEquals(notClassEvaluator.toString(), ".myClass");
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div:not(.myClass):has(p:containsOwn(text))";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(evaluator.toString(), query);

        Evaluator notClassEvaluator = ((CombiningEvaluator.And) evaluator).getChild(0);
        assertTrue(notClassEvaluator instanceof StructuralEvaluator.Not);
        assertEquals(notClassEvaluator.toString(), ":not(.myClass)");

        Evaluator hasEvaluator = ((CombiningEvaluator.And) evaluator).getChild(1);
        assertTrue(hasEvaluator instanceof StructuralEvaluator.Has);
        assertEquals(hasEvaluator.toString(), "p:containsOwn(text)");
    }
}