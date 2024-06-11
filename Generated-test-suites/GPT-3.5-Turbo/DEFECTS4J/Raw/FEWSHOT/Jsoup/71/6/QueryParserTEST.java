package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.tagName);
        
        evaluator = QueryParser.parse("#id");
        assertTrue(evaluator instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("id", idEvaluator.id);
        
        evaluator = QueryParser.parse(".class");
        assertTrue(evaluator instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals("class", classEvaluator.className);
        
        evaluator = QueryParser.parse("[attr=value]");
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeWithValueEvaluator = (Evaluator.AttributeWithValue) evaluator;
        assertEquals("attr", attributeWithValueEvaluator.key);
        assertEquals("value", attributeWithValueEvaluator.value);
        
        evaluator = QueryParser.parse(":contains(text)");
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        Evaluator.ContainsText containsTextEvaluator = (Evaluator.ContainsText) evaluator;
        assertEquals("text", StringUtil.unescape(containsTextEvaluator.text));
        
        evaluator = QueryParser.parse("*");
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }
}