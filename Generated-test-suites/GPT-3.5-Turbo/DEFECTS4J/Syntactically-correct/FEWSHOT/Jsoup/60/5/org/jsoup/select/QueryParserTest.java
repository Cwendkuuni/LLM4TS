package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse(".test-class");
        assertTrue(evaluator instanceof Evaluator.Class);
        
        evaluator = QueryParser.parse("#test-id");
        assertTrue(evaluator instanceof Evaluator.Id);
        
        evaluator = QueryParser.parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
        
        evaluator = QueryParser.parse("[data-test]");
        assertTrue(evaluator instanceof Evaluator.Attribute);
        
        evaluator = QueryParser.parse(":contains(test)");
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        
        evaluator = QueryParser.parse(":has(.child-class)");
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        
        evaluator = QueryParser.parse(":matches(regex)");
        assertTrue(evaluator instanceof Evaluator.Matches);
        
        evaluator = QueryParser.parse(":not(.exclude-class)");
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }
}