package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        // Test case 1
        Evaluator evaluator1 = QueryParser.parse("div");
        assertTrue(evaluator1 instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator1).tagName);

        // Test case 2
        Evaluator evaluator2 = QueryParser.parse("#id");
        assertTrue(evaluator2 instanceof Evaluator.Id);
        assertEquals("id", ((Evaluator.Id) evaluator2).id);

        // Test case 3
        Evaluator evaluator3 = QueryParser.parse(".class");
        assertTrue(evaluator3 instanceof Evaluator.Class);
        assertEquals("class", ((Evaluator.Class) evaluator3).className);

        // Test case 4
        Evaluator evaluator4 = QueryParser.parse("[attr]");
        assertTrue(evaluator4 instanceof Evaluator.Attribute);
        assertEquals("attr", ((Evaluator.Attribute) evaluator4).key);

        // Test case 5
        Evaluator evaluator5 = QueryParser.parse("[attr=value]");
        assertTrue(evaluator5 instanceof Evaluator.AttributeWithValue);
        assertEquals("attr", ((Evaluator.AttributeWithValue) evaluator5).key);
        assertEquals("value", ((Evaluator.AttributeWithValue) evaluator5).value);

        // Test case 6
        Evaluator evaluator6 = QueryParser.parse(":has(selector)");
        assertTrue(evaluator6 instanceof StructuralEvaluator.Has);
        assertNotNull(((StructuralEvaluator.Has) evaluator6).hasEvaluator);

        // Test case 7
        Evaluator evaluator7 = QueryParser.parse(":contains(text)");
        assertTrue(evaluator7 instanceof Evaluator.ContainsText);
        assertEquals("text", ((Evaluator.ContainsText) evaluator7).searchText);

        // Test case 8
        Evaluator evaluator8 = QueryParser.parse(":matches(regex)");
        assertTrue(evaluator8 instanceof Evaluator.Matches);
        assertEquals("regex", ((Evaluator.Matches) evaluator8).regex.pattern());

        // Test case 9
        Evaluator evaluator9 = QueryParser.parse(":not(selector)");
        assertTrue(evaluator9 instanceof StructuralEvaluator.Not);
        assertNotNull(((StructuralEvaluator.Not) evaluator9).notEvaluator);
    }
}