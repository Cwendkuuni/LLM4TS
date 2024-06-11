package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
        
        evaluator = QueryParser.parse(".class");
        assertTrue(evaluator instanceof Evaluator.Class);
        
        evaluator = QueryParser.parse("#id");
        assertTrue(evaluator instanceof Evaluator.Id);
        
        evaluator = QueryParser.parse("[attr=value]");
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        
        evaluator = QueryParser.parse(":contains(text)");
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        
        evaluator = QueryParser.parse(":has(selector)");
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        
        evaluator = QueryParser.parse(":matches(regex)");
        assertTrue(evaluator instanceof Evaluator.Matches);
        
        evaluator = QueryParser.parse(":not(selector)");
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }
    
    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidQuery() {
        QueryParser.parse("");
    }
    
    @Test
    public void testParseWithCombinators() {
        Evaluator evaluator = QueryParser.parse("div > .class");
        
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And combiningEvaluator = (CombiningEvaluator.And) evaluator;
        assertEquals(2, combiningEvaluator.evaluators().size());
        
        Evaluator evaluator1 = combiningEvaluator.evaluators().get(0);
        assertTrue(evaluator1 instanceof Evaluator.Tag);
        
        Evaluator evaluator2 = combiningEvaluator.evaluators().get(1);
        assertTrue(evaluator2 instanceof CombiningEvaluator.And);
        
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator2;
        assertEquals(2, andEvaluator.evaluators().size());
    }
}