package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse(".class");
        assertEquals("Evaluator.Class(.class)", evaluator.toString());
        
        evaluator = QueryParser.parse("#id");
        assertEquals("Evaluator.Id(#id)", evaluator.toString());
        
        evaluator = QueryParser.parse("tag");
        assertEquals("Evaluator.Tag(tag)", evaluator.toString());
        
        evaluator = QueryParser.parse("[attr]");
        assertEquals("Evaluator.Attribute(attr)", evaluator.toString());
        
        evaluator = QueryParser.parse("[attr=value]");
        assertEquals("Evaluator.AttributeWithValue(attr, value)", evaluator.toString());
        
        evaluator = QueryParser.parse(".class[attr=value]");
        assertEquals("Evaluator.Class(.class), Evaluator.AttributeWithValue(attr, value)", evaluator.toString());
        
        evaluator = QueryParser.parse(".class[attr=value]:contains(text)");
        assertEquals("Evaluator.Class(.class), Evaluator.AttributeWithValue(attr, value), Evaluator.ContainsText(text)", evaluator.toString());
        
        evaluator = QueryParser.parse(".class[attr=value]:has(tag)");
        assertEquals("Evaluator.Class(.class), Evaluator.AttributeWithValue(attr, value), StructuralEvaluator.Has(Evaluator.Tag(tag))", evaluator.toString());
        
        evaluator = QueryParser.parse(".class:not(tag)");
        assertEquals("Evaluator.Class(.class), StructuralEvaluator.Not(Evaluator.Tag(tag))", evaluator.toString());
        
        assertNull(QueryParser.parse(""));
    }
}