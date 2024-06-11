package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        
        evaluator = QueryParser.parse("#id");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        
        evaluator = QueryParser.parse(".classname");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        
        evaluator = QueryParser.parse("[name=value]");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        
        evaluator = QueryParser.parse("div.classname");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        
        evaluator = QueryParser.parse("div#id.classname");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        
        evaluator = QueryParser.parse("div:nth-child(2)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        
        evaluator = QueryParser.parse(":first-child");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        
        evaluator = QueryParser.parse(":last-child");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
        
        evaluator = QueryParser.parse(":empty");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
        
        evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        
        evaluator = QueryParser.parse(":has(selector)");
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }
    
}