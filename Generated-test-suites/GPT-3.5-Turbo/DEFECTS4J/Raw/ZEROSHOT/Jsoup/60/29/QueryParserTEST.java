package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator result = QueryParser.parse("body");
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Tag);
        assertEquals("body", ((Evaluator.Tag) result).tagName());
    }

    @Test
    public void testParseWithCombinator() {
        Evaluator result = QueryParser.parse("div > span");
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        assertTrue(andEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) andEvaluator.evaluators().get(0)).tagName());
        assertTrue(andEvaluator.evaluators().get(1) instanceof StructuralEvaluator.ImmediateParent);
        assertTrue(((StructuralEvaluator.ImmediateParent) andEvaluator.evaluators().get(1)).parentEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((StructuralEvaluator.ImmediateParent) andEvaluator.evaluators().get(1)).parentEvaluator()).tagName());
    }

    @Test
    public void testParseWithAttribute() {
        Evaluator result = QueryParser.parse("[name='test']");
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeEvaluator = (Evaluator.AttributeWithValue) result;
        assertEquals("name", attributeEvaluator.key());
        assertEquals("test", attributeEvaluator.value());
    }
}