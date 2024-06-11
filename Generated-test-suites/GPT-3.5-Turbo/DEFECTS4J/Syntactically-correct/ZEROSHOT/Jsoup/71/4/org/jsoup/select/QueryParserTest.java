
package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("myId", idEvaluator.getId());
    }

    @Test
    public void testCombinator() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        assertEquals(2, andEvaluator.evaluators().size());
        assertTrue(andEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(andEvaluator.evaluators().get(1) instanceof StructuralEvaluator.ImmediateParent);
    }

    @Test
    public void testFindElements() {
        String query = "div, p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) evaluator;
        assertEquals(2, orEvaluator.evaluators().size());
        assertTrue(orEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(orEvaluator.evaluators().get(1) instanceof Evaluator.Tag);
    }

    @Test
    public void testById() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("myId", idEvaluator.getId());
    }

    @Test
    public void testByClass() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals("myClass", classEvaluator.getClassName());
    }

    @Test
    public void testByTag() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.getTagName());
    }

    @Test
    public void testByAttribute() {
        String query = "[name=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeEvaluator = (Evaluator.AttributeWithValue) evaluator;
        assertEquals("name", attributeEvaluator.getKey());
        assertEquals("value", attributeEvaluator.getValue());
    }
    
    // Add more test methods for other methods in QueryParser class
    
}

