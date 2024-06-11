package org.jsoup.select;

import org.jsoup.nodes.Element;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class CombiningEvaluatorTest {
    
    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("p");
        Evaluator evaluator2 = new Evaluator.Tag("div");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        
        assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator.Tag("p");
        Evaluator evaluator2 = new Evaluator.Tag("div");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        
        assertEquals("Tag: p Tag: div", andEvaluator.toString());
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("p");
        Evaluator evaluator2 = new Evaluator.Tag("div");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator.Tag("p");
        Evaluator evaluator2 = new Evaluator.Tag("div");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        
        assertEquals(":or[Tag: p, Tag: div]", orEvaluator.toString());
    }
}