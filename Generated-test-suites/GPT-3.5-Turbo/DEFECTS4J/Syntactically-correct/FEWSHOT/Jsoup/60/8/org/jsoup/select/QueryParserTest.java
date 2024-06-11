package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());

        evaluator = QueryParser.parse("#id");
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id", ((Evaluator.Id) evaluator).getId());

        evaluator = QueryParser.parse(".class");
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class", ((Evaluator.Class) evaluator).getClassName());

        evaluator = QueryParser.parse("[attr]");
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("attr", ((Evaluator.Attribute) evaluator).getKey());

        evaluator = QueryParser.parse("div > p");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        assertTrue(andEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(andEvaluator.evaluators().get(1) instanceof StructuralEvaluator.ImmediateParent);

        evaluator = QueryParser.parse("div + p");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        andEvaluator = (CombiningEvaluator.And) evaluator;
        assertTrue(andEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(andEvaluator.evaluators().get(1) instanceof StructuralEvaluator.ImmediatePreviousSibling);

        evaluator = QueryParser.parse("div ~ p");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        andEvaluator = (CombiningEvaluator.And) evaluator;
        assertTrue(andEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(andEvaluator.evaluators().get(1) instanceof StructuralEvaluator.PreviousSibling);

        evaluator = QueryParser.parse("div, p");
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) evaluator;
        assertTrue(orEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(orEvaluator.evaluators().get(1) instanceof Evaluator.Tag);
    }
}