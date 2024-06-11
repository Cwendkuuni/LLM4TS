package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.test");
        assertTrue(evaluator instanceof Evaluator.And);
        assertEquals(2, ((Evaluator.And) evaluator).getEvaluators().size());
        
        evaluator = QueryParser.parse("input[type=button]");
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("type", ((Evaluator.AttributeWithValue) evaluator).getKey());
        assertEquals("button", ((Evaluator.AttributeWithValue) evaluator).getValue());
        
        evaluator = QueryParser.parse("div.test input[name=foo]");
        assertTrue(evaluator instanceof Evaluator.Descendant);
        assertEquals(2, ((Evaluator.Descendant) evaluator).getEvaluators().size());
        
        evaluator = QueryParser.parse("div:first-child");
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        
        evaluator = QueryParser.parse(":not(div)");
        assertTrue(evaluator instanceof Evaluator.Not);
        assertTrue(((Evaluator.Not) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        
        evaluator = QueryParser.parse(":contains(text)");
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("text", ((Evaluator.ContainsText) evaluator).getText());
    }
}