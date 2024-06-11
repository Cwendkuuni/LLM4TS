package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Document doc = Parser.parseBodyFragment("<div id=\"div1\"><span class=\"class1\"></span></div>", "");
        Element div = doc.getElementById("div1");
        Element span = doc.getElementsByClass("class1").first();
        
        Evaluator evaluator1 = new Evaluator.Id("div1");
        Evaluator evaluator2 = new Evaluator.Class("class1");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        
        assertTrue(andEvaluator.matches(div, div));
        assertFalse(andEvaluator.matches(div, span));
        assertFalse(andEvaluator.matches(span, div));
        assertTrue(andEvaluator.matches(span, span));
    }
    
    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        
        assertEquals("div span", andEvaluator.toString());
    }
    
    @Test
    public void testOrMatches() {
        Document doc = Parser.parseBodyFragment("<div id=\"div1\"><span class=\"class1\"></span></div>", "");
        Element div = doc.getElementById("div1");
        Element span = doc.getElementsByClass("class1").first();
        
        Evaluator evaluator1 = new Evaluator.Id("div1");
        Evaluator evaluator2 = new Evaluator.Class("class1");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        
        assertTrue(orEvaluator.matches(div, div));
        assertTrue(orEvaluator.matches(div, span));
        assertTrue(orEvaluator.matches(span, div));
        assertTrue(orEvaluator.matches(span, span));
    }
    
    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        
        assertEquals(":or[div, span]", orEvaluator.toString());
    }
}